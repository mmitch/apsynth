package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;

public class SampleClass extends DefaultSignalClass {

    public SampleClass() {
		this.paramCount = 0;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
        return new Sample();
	}
	
	public String getName() {
		return "Sample";
	}

    public static class Sample implements Signal {

        private de.cgarbs.apsynth.Sample sample = null;

        public Stereo get(long tick, long local) {

        	if (sample == null) {
        		return new Stereo();
        	}
        	return sample.get(local);
        	
        }

    	public void setSample(String name) {
    		this.sample = Pool.getSample(name);
    	}
    	
        public boolean isEnveloped() {
            return false;
        }

    }
}
