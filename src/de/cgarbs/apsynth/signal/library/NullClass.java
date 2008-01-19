package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;

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

    	Stereo nul = new Stereo();  
    	
        private Null () {
            // no-op: no parameters 
        }

        public Stereo get(long tick, long local) {
            return nul;
        }

        public boolean isEnveloped() {
            return false;
        }

    }
}
