package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
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
	public Signal instanciate(Signal[] s) {
		checkParams(s);
		return new FiniteImpulseResponse(s[0], s[1]);
	}
	
	public String getName() {
		return "FiniteImpulseResponse";
	}

    public class FiniteImpulseResponse implements Signal {

        private Signal signal = null;
        private int tapcount; 
        private double tap[];
        private double buffer[];
        private int head = 0; 
        
        private FiniteImpulseResponse(Signal signal, Signal data) {

        	if (!(data instanceof DataBlock)) {
                throw new RuntimeException(this.getClass().getName() + " called without DataBlock!");
        	}
        	
        	this.signal = signal;
            this.head = 0;
            this.tapcount = ((DataBlock)data).getLength();
            this.tap = new double[tapcount*2];
            this.buffer= new double[tapcount];
            
            // fill taps (data is only used in this constructor)
            for (int i=0; i<this.tapcount; i++) {
            	tap[i] = data.get(i);
            }
            
            // duplicate coefficient table (optimization)
            for (int i=0, j=this.tapcount; i<this.tapcount; i++,j++) {
                this.tap[j] = this.tap[i];
            }
        }

        public double get(long tick) {

            // store new signal in ringbuffer
            head++;
            if (head == tapcount) {
                head = 0;
            }
            buffer[head] = signal.get(tick);
            
            // add all taps
            double sum = 0;
            for (int i=0,j=tapcount-head; i<tapcount; i++,j++) {
                sum += buffer[i] * tap[j];
            }
            
            return sum;
        }

    }
}
