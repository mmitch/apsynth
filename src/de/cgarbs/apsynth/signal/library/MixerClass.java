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
            if (s[1] instanceof ConstantSignal) {
                return ConstantSignalClass.get(s[0].get(0,0)+s[1].get(0,0));
            } else {
                return new ConstantMixer(s[1], s[0]);
            }
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
        
        public double get(long t, long l) {
            return s1.get(t,l) + s2.get(t,l);
        }

        private Mixer(Signal s1, Signal s2) {
            this.s1 = s1;
            this.s2 = s2;
        }
        
    }

    public class ConstantMixer implements Signal {

        private Signal s1;
        private double s2;
        
        public double get(long t, long l) {
            return s1.get(t,l) + s2;
        }

        private ConstantMixer(Signal s1, Signal s2) {
            this.s1 = s1;
            this.s2 = s2.get(0,0);
        }
        
    }
}
