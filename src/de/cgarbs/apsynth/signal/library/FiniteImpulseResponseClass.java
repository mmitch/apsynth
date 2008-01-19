package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;
import de.cgarbs.apsynth.signal.library.DataBlockClass.DataBlock;

public class FiniteImpulseResponseClass extends DefaultSignalClass {

    /*
     * see http://www.dspguru.com/info/faqs/fir/basics.htm for a FAQ on
     * FIR (Finite Impulse Response)
     * 
     * This implements the "duplicate coefficient" optimization 
     */
    
	public FiniteImpulseResponseClass() {
		this.paramCount = 2;
	}
	
    /**
     * 1: signal
     * 2: TAP data (must be a DataBlock!)
     */    
	public Signal instantiate(Signal[] s) {
		checkParams(s);
		return new FiniteImpulseResponse(s[0], s[1]);
	}
	
	public String getName() {
		return "FiniteImpulseResponse";
	}

    public static class FiniteImpulseResponse implements Signal {

        private Signal signal = null;
        private int tapcount; 
        private int oldtapcount = 0;
        private double tap[];
        private Stereo buffer[];
        private int head; 
        private boolean enveloped;
        
        public FiniteImpulseResponse(Signal signal, Signal data) {

        	this.signal = signal;
            this.head = 0;
        	updateTaps(data);
            
            enveloped = signal.isEnveloped();
        }

        public void updateTaps(Signal data) {
            	
        	if (!(data instanceof DataBlock)) {
                throw new RuntimeException(this.getClass().getName() + " called without DataBlock!");
        	}
        	
            this.tapcount = ((DataBlock)data).getLength();
            this.tap = new double[tapcount*2];
            
            // fill taps (data is only used in this constructor)
            for (int i=0; i<this.tapcount; i++) {
            	tap[i] = data.get(0, i).getMono();
            }
            
            // duplicate coefficient table (optimization)
            for (int i=0, j=this.tapcount; i<this.tapcount; i++,j++) {
                this.tap[j] = this.tap[i];
            }

            if (oldtapcount < tapcount) {
            	Stereo[] newBuffer = new Stereo[tapcount];
            	for (int i=0; i<oldtapcount; i++) {
            		newBuffer[i] = buffer[i];
            	}
            	for (int i=oldtapcount; i<tapcount; i++) {
            		newBuffer[i] = new Stereo();
            	}
            	this.buffer = newBuffer;
            } else if (oldtapcount > tapcount) {
            	Stereo[] newBuffer = new Stereo[tapcount];
            	for (int i=0; i<tapcount; i++) {
            		newBuffer[i] = buffer[i];
            	}
            	this.buffer = newBuffer;
            }

        }
        
        public Stereo get(long tick, long local) {

            // store new signal in ringbuffer
            head++;
            if (head >= tapcount) {
                head = 0;
            }
            buffer[head] = signal.get(tick, local);
            
            // add all taps
            Stereo sum = new Stereo();
            for (int i=0,j=tapcount-head; i<tapcount; i++,j++) {
                sum.l += buffer[i].l * tap[j];
                sum.r += buffer[i].r * tap[j];
            }
            
            return sum;
        }

        public boolean isEnveloped() {
            return enveloped;
        }

    }
}
