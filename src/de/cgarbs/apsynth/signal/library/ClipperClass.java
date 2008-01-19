package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass.ConstantSignal;

public class ClipperClass extends DefaultSignalClass {

    /**
     * 1: signal
     * 2: maximum value
     */
	public ClipperClass() {
		this.paramCount = 2;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
        if (s[1] instanceof ConstantSignal) {
            return new ConstantClipper(s[0], s[1]);
        }
        return new Clipper(s[0], s[1]);
	}
	
	public String getName() {
		return "Clipper";
	}

    public static class Clipper implements Signal {

        private Signal signal;
        private Signal clip;
        private boolean enveloped;
        
        public Stereo get(long tick, long local) {
            Stereo s = signal.get(tick, local);
            Stereo c = clip.get(tick, local);
            Stereo ret = new Stereo(s);
            if (s.l > c.l) {
                ret.l = c.l;
            } else if (s.l < -c.l) {
                ret.l = -c.l;
            }
            if (s.r > c.r) {
                ret.r = c.r;
            } else if (s.r < -c.r) {
                ret.r = -c.r;
            }
            return ret;
        }

        /**
         * 1: signal
         * 2: maximum value
         */
        private Clipper(Signal signal, Signal clip) {
            this.signal = signal;
            this.clip   = clip;
            this.enveloped = signal.isEnveloped();
        }

        public boolean isEnveloped() {
            return enveloped;
        }

    }

    public class ConstantClipper implements Signal {

        private Signal signal;
        private double clipPositive_l;
        private double clipNegative_l;
        private double clipPositive_r;
        private double clipNegative_r;
        private boolean enveloped;
        
        public Stereo get(long tick, long local) {
            Stereo s = new Stereo(signal.get(tick, local));
            if (s.l > clipPositive_l) {
                s.l = clipPositive_l;
            } else if (s.l < clipNegative_l) {
                s.l = clipNegative_l;
            }
            if (s.r > clipPositive_r) {
                s.r = clipPositive_r;
            } else if (s.r < clipNegative_r) {
                s.r = clipNegative_r;
            }
            return s;
        }

        /**
         * 1: signal
         * 2: maximum value
         */
        private ConstantClipper(Signal signal, Signal clip) {
            this.signal = signal;
            this.clipPositive_l = clip.get(0, 0).l;
            this.clipNegative_l = -clipPositive_l;
            this.clipPositive_r = clip.get(0, 0).r;
            this.clipNegative_r = -clipPositive_r;
            this.enveloped = signal.isEnveloped();
        }

        public boolean isEnveloped() {
            return enveloped;
        }

    }
}
