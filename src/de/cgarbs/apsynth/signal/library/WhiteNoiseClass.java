package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;

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

        public double get(long tick, long local) {
            return Math.random() * 2 - 1;
        }

    }
}
