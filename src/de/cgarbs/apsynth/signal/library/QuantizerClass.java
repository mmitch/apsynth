package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;

public class QuantizerClass extends DefaultSignalClass {

    // TODO constant optimization
    
	public QuantizerClass() {
		this.paramCount = 2;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
		return new Quantizer(s[0], s[1]);
	}
	
	public String getName() {
		return "Quantizer";
	}

    public static class Quantizer implements Signal {

        private Signal signal;
        private Signal quant;
        
        public double get(long tick, long local) {
            double q = quant.get(tick, local); // cache for speedup
            return Math.round( signal.get(tick, local) * q ) / q;
        }

        /**
         * 1: signal
         * 2: quantization factor (possible different states)
         */
        private Quantizer(Signal signal, Signal quant) {
            this.signal = signal;
            this.quant  = quant;
        }

    }
}
