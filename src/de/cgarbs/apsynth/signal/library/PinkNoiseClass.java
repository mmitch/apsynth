package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;

public class PinkNoiseClass extends DefaultSignalClass {

	public PinkNoiseClass() {
		this.paramCount = 0;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
		return new PinkNoise();
	}
	
	public String getName() {
		return "PinkNoise";
	}

    public static class PinkNoise implements Signal {

    	// see http://www.firstpr.com.au/dsp/pink-noise/ for background information
    	
    	// this is a self-written implementation of Paul Kellets pink noise
    	// approximation as described in
    	// http://shoko.calarts.edu/%7Eglmrboy/musicdsp/sourcecode/pink.txt
    	
    	// TODO does not work correctly yet!
    	
    	private double b0 = 0;
    	private double b1 = 0;
    	private double b2 = 0;
    	private double b3 = 0;
    	private double b4 = 0;
    	private double b5 = 0;
    	private double b6 = 0;
    	
        private PinkNoise () {
            // no-op: no parameters 
        }

        public double get(long tick) {
        	double pink;
        	b0 = 0.99886 * b0 + Math.random() * 0.0555179;
        	b1 = 0.99332 * b1 + Math.random() * 0.0750759;
        	b2 = 0.96900 * b2 + Math.random() * 0.1538520;
        	b3 = 0.86650 * b3 + Math.random() * 0.3104856;
        	b4 = 0.55000 * b4 + Math.random() * 0.5329522;
        	b5 = -0.7616 * b5 - Math.random() * 0.0168980;
        	pink = b0 + b1 + b2 + b3 + b4 + b5 + b6 + Math.random() * 0.5362;
        	b6 = Math.random() * 0.115926;
        	return pink * 2 - 1;
        }

    }
}
