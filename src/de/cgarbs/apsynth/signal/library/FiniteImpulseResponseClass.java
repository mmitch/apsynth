package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;

public class FiniteImpulseResponseClass extends DefaultSignalClass {

    /*
     * see http://www.dspguru.com/info/faqs/fir/basics.htm for a FAQ on
     * FIR (Finite Impulse Response)
     * 
     * This implements the "duplicate coefficient" 
     */
    
	public FiniteImpulseResponseClass() {
		this.paramCount = 1;
	}
	
    /**
     * 1: signal
     * 2: delay buffer size [ms] (const)
     * 3: amplification
     * 4: reamplification 
     */    
	public Signal instanciate(Signal[] s) {
		checkParams(s);
		return new FiniteImpulseResponse(s[0]);
	}
	
	public String getName() {
		return "FiniteImpulseResponse";
	}

    public class FiniteImpulseResponse implements Signal {

        private Signal signal = null;
        private int tapcount = 10; 
        private double tap[];
        private double buffer[];
        private int head = 0; 
        
        private FiniteImpulseResponse(Signal signal) {
            this.signal = signal;
            this.head = 0;
            this.tap = new double[tapcount*2];
            this.buffer= new double[tapcount];
            for (int i=0; i<this.tapcount; i++) {
                this.tap[i]= (i%2 == 0) ? 0.2 : 0;
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
