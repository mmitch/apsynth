package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;

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
        private boolean enveloped;
        
        public Stereo get(long tick, long local) {
            double q = quant.get(tick, local).getMono(); // cache for speedup
            return new Stereo(Math.round( signal.get(tick, local).l * q ) / q,
            		          Math.round( signal.get(tick, local).r * q ) / q);
        }

        /**
         * 1: signal
         * 2: quantization factor (possible different states)
         */
        private Quantizer(Signal signal, Signal quant) {
            this.signal = signal;
            this.quant  = quant;
            enveloped = signal.isEnveloped();
        }

        public boolean isEnveloped() {
            return enveloped;
        }

    }
}
