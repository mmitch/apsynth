package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass.ConstantSignal;

public class StereoPositionClass extends DefaultSignalClass {

	public StereoPositionClass() {
		this.paramCount = 2;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
        if (s[1] instanceof ConstantSignal) {
            return new ConstantStereoPosition(s[0], s[1]);
        }
		return new StereoPosition(s[0], s[1]);
	}
	
	public String getName() {
		return "StereoPosition";
	}

    public static class StereoPosition implements Signal {

        private Signal s1;
        private Signal s2;
        private boolean enveloped;
        
        public Stereo get(long t, long l) {
        	Stereo ret = new Stereo(s1.get(t,l));
        	double pos = s2.get(t, l).getMono();
        	if (pos > 0 && pos <= 1) {
        		ret.l *= (1-pos);
        	}
        	if (pos < 0 && pos >= -1) {
        		ret.r *= (1+pos);
        	}
            return ret;
        }

        private StereoPosition(Signal s1, Signal s2) {
            this.s1 = s1;
            this.s2 = s2;
            enveloped = s1.isEnveloped() || s2.isEnveloped();
        }
        
        public boolean isEnveloped() {
            return enveloped;
        }

    }

    public class ConstantStereoPosition implements Signal {

        private Signal s1;
        private double f_l = 1;
        private double f_r = 1;
        private boolean enveloped;
        
        public Stereo get(long t, long l) {
        	Stereo cache = s1.get(t,l);
            return new Stereo(cache.l * f_l, cache.r * f_r);
        }

        private ConstantStereoPosition(Signal s1, Signal s2) {
            this.s1 = s1;
            double pos = s2.get(0,0).getMono();
        	if (pos > 0 && pos <= 1) {
        		f_l = (1-pos);
        	}
        	if (pos < 0 && pos >= -1) {
        		f_r *= (1+pos);
        	}
            enveloped = s1.isEnveloped();
        }
        
        public boolean isEnveloped() {
            return enveloped;
        }

    }
}
