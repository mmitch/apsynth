package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;

public class NullClass extends DefaultSignalClass {

	public NullClass() {
		this.paramCount = 0;
	}
	
	public Signal instanciate(Signal[] s) {
		checkParams(s);
		return new Null();
	}
	
	public String getName() {
		return "Null";
	}

    public class Null implements Signal {

        private Null () {
            // no-op: no parameters 
        }

        public double get(long tick) {
            return 0;
        }

    }
}
