package de.cgarbs.apsynth.signal.library;

import java.util.HashMap;
import de.cgarbs.apsynth.Apsynth;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.DataBlockClass.DataBlock;
import de.cgarbs.apsynth.signal.library.FiniteImpulseResponseClass.FiniteImpulseResponse;
import dsp.RemezFIRFilter;

public class HighPassClass extends DefaultSignalClass {

	public HighPassClass() {
		this.paramCount = 5;
	}
	
    /**
     * 0: input signal
     * 1: cutoff frequency [Hz]
     * 2: transition bandwidth [Hz]
     * 3: stopband attenuation [dB]
     * 4: passband ripple [dB]
     */
	public Signal instantiate(Signal[] s) {
		checkParams(s);

        /* TODO: optimize
        if (s[1].get(0)-s[2].get(0) > Apsynth.samplefreq/2) {
            // cutoff is over nyquist frequency
        }
        */
		return new HighPass(s[0], s[1], s[2], s[3], s[4]);
	}
	
	public String getName() {
		return "HighPass";
	}

    public static class HighPass implements Signal {

        private FiniteImpulseResponse filter;
        private int maxTaps = 0;
        private Signal s_cutoff;
        private Signal s_trband;
        private Signal s_atten;
        private Signal s_ripple;
        
        private HashMap<String,DataBlock> cache = new HashMap<String,DataBlock>();
        private String lastKey;
        private String newKey;
        
        public double get(long t) {
        	newKey = getHashKey(t);
        	if (! newKey.equals(lastKey) ) {
        		update_filter(t);
        	}
            return filter.get(t);
        }

        /**
         * 0: input signal
         * 1: cutoff frequency [Hz]
         * 2: transition bandwidth [Hz]
         * 3: stopband attenuation [dB]
         * 4: passband ripple [dB]
         */
        private HighPass(Signal signal, Signal s_cutoff, Signal s_trband, Signal s_atten, Signal s_ripple) {

            // TODO: make filters changeable after initialization (lookout: needs cache)
        	this.s_cutoff = s_cutoff;
        	this.s_trband = s_trband;
        	this.s_atten  = s_atten;
        	this.s_ripple = s_ripple;
        	
        	lastKey = "";

            this.filter = new FiniteImpulseResponse(signal, new DataBlock(new double[]{1}));
        }
        
        private String getHashKey(long t) {
        	StringBuffer b = new StringBuffer();
        	b.append(maxTaps).append(':');
        	b.append(s_cutoff.get(t)).append(':');
        	b.append(s_trband.get(t)).append(':');
        	b.append(s_atten.get(t) ).append(':');
        	b.append(s_ripple.get(t)).append(':');
        	return b.toString();
        }
        
        void update_filter(long t) {
        	
        	if (cache.containsKey(newKey)) {
        		filter.updateTaps(cache.get(newKey));
        	} else {
        	
	            // taken from RemezFIRFilterDesign.java
	            int numBands = 2;
	            double[] desired = new double[numBands];
	            double[] bands   = new double[2*numBands];
	            double[] weights = new double[numBands];
	
	            double f1 = s_cutoff.get(t) / Apsynth.samplefreq;
	            double trband = s_trband.get(t) / Apsynth.samplefreq;
	            
	            double atten = s_atten.get(t);
	            double ripple = s_ripple.get(t);
	            double deltaP = 0.5f*(1.0f - (double)Math.pow(10.0f, -0.05f*ripple));
	            double deltaS = (double)Math.pow(10.0f, -0.05f*atten);
	            double rippleRatio = deltaP / deltaS;
	            
	            desired[0] = 0.0;
	            desired[1] = 1.0;
	            bands[0] = 0.0;
	            bands[1] = f1 - trband;
	            bands[2] = f1;
	            bands[3] = 0.5;
	            weights[0] = rippleRatio;
	            weights[1] = 1.0;
	
	            if (bands[2] > bands[3]) {
	                bands[2] = bands[3];
	            }
	            if (bands[1] > bands[2]) {
	                bands[1] = bands[2];
	            }
	            
	            int numTaps = (int)Math.round((-10.0d*(Math.log(deltaP*deltaS)/Math.log(10.0d)) - 13)/(14.6d*trband));
	            if (numTaps < maxTaps) {
	            	numTaps = maxTaps;
	            } else if (numTaps > maxTaps) {
	            	maxTaps = numTaps;
	            }
	            
	            DataBlock coeff = new DataBlock(
	                    new RemezFIRFilter().remez(numTaps, bands, desired, weights, RemezFIRFilter.BANDPASS)
	            );
	
	            cache.put(newKey, coeff);
	            
	            filter.updateTaps(coeff);
        	}
        	
        	lastKey = newKey;

    		System.out.println("recalibrate!!! taps = "+maxTaps + ":" + newKey);
    		
        }
                
    }
}
