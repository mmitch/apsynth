package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;

public class WhiteNoiseClass extends DefaultSignalClass {

	public WhiteNoiseClass() {
		this.paramCount = 0;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
		return new WhiteNoise();
	}
	
	public String getName() {
		return "WhiteNoise";
	}

    public static class WhiteNoise implements Signal {

        private WhiteNoise () {
            // no-op: no parameters 
        }

        public Stereo get(long tick, long local) {
            return new Stereo(Math.random() * 2 - 1, Math.random() * 2 - 1);
        }

        public boolean isEnveloped() {
            return false;
        }

    }
}
