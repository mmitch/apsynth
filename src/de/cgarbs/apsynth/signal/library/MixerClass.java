package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass.ConstantSignal;

public class MixerClass extends DefaultSignalClass {

	public MixerClass() {
		this.paramCount = 2;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
        if (s[0] instanceof ConstantSignal) {
            if (s[1] instanceof ConstantSignal) {
                return ConstantSignalClass.get(s[0].get(0,0).mix(s[1].get(0,0)));
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
        private boolean enveloped;
        
        public Stereo get(long t, long l) {
            return new Stereo(s1.get(t,l).mix(s2.get(t,l)));
        }

        private Mixer(Signal s1, Signal s2) {
            this.s1 = s1;
            this.s2 = s2;
            enveloped = s1.isEnveloped() || s2.isEnveloped();
        }
        
        public boolean isEnveloped() {
            return enveloped;
        }

    }

    public class ConstantMixer implements Signal {

        private Signal s1;
        private Stereo s2;
        private boolean enveloped;
        
        public Stereo get(long t, long l) {
            return new Stereo(s1.get(t,l).mix(s2));
        }

        private ConstantMixer(Signal s1, Signal s2) {
            this.s1 = s1;
            this.s2 = s2.get(0,0);
            enveloped = s1.isEnveloped();
        }
        
        public boolean isEnveloped() {
            return enveloped;
        }

    }
}
