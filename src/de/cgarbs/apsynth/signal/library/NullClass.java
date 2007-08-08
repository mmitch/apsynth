package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;

public class NullClass extends DefaultSignalClass {

	public NullClass() {
		this.paramCount = 0;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
		return new Null();
	}
	
	public String getName() {
		return "Null";
	}

    public static class Null implements Signal {

        private Null () {
            // no-op: no parameters 
        }

        public double get(long tick, long local) {
            return 0;
        }

        public boolean isEnveloped() {
            return false;
        }

    }
}
