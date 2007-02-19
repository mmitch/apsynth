package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass.ConstantSignal;

public class MixerClass extends DefaultSignalClass {

	public MixerClass() {
		this.paramCount = 2;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
        if (s[0] instanceof ConstantSignal) {
            return new ConstantMixer(s[1], s[0]);
        } else if (s[1] instanceof ConstantSignal) {
            return new ConstantMixer(s[0], s[1]);

        }
		return new Mixer(s[0], s[1]);
	}
	
	public String getName() {
		return "Mixer";
	}

    public static class Mixer implements Signal {

        private Signal s1;
        private Signal s2;
        
        public double get(long t) {
            return s1.get(t) + s2.get(t);
        }

        private Mixer(Signal s1, Signal s2) {
            this.s1 = s1;
            this.s2 = s2;
        }
        
    }

    public class ConstantMixer implements Signal {

        private Signal s1;
        private double s2;
        
        public double get(long t) {
            return s1.get(t) + s2;
        }

        private ConstantMixer(Signal s1, Signal s2) {
            this.s1 = s1;
            this.s2 = s2.get(0);
        }
        
    }
}
