package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass.ConstantSignal;

public class AmplifierClass extends DefaultSignalClass {

	public AmplifierClass() {
		this.paramCount = 2;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
        if (s[0] instanceof ConstantSignal) {
            return new ConstantAmplifier(s[1], s[0]);
        } else if (s[1] instanceof ConstantSignal) {
            return new ConstantAmplifier(s[0], s[1]);
        }
		return new Amplifier(s[0], s[1]);
	}
	
	public String getName() {
		return "Amplifier";
	}

    public static class Amplifier implements Signal {

        private Signal s1;
        private Signal s2;
        private boolean enveloped;
        
        public Stereo get(long t, long l) {
            return new Stereo(s1.get(t, l).l * s2.get(t, l).l,
            		          s1.get(t, l).r * s2.get(t, l).r);
        }

        private Amplifier(Signal s1, Signal s2) {
            this.s1 = s1;
            this.s2 = s2;
            this.enveloped = s1.isEnveloped() || s2.isEnveloped();
        }

        public boolean isEnveloped() {
            return enveloped;
        }
        
    }

    public class ConstantAmplifier implements Signal {

        private Signal s1;
        private Stereo s2;
        private boolean enveloped;
        
        public Stereo get(long t, long l) {
            return new Stereo(s1.get(t, l).l * s2.l,
            		          s1.get(t, l).r * s2.r);
        }

        private ConstantAmplifier(Signal s1, Signal s2) {
            this.s1 = s1;
            this.s2 = s2.get(0, 0);
            this.enveloped = s1.isEnveloped();
        }

        public boolean isEnveloped() {
            return enveloped;
        }
        
    }
}
