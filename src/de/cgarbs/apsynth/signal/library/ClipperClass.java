package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
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

    public class Clipper implements Signal {

        private Signal signal;
        private Signal clip;
        
        public double get(long tick) {
            double s = signal.get(tick);
            double c = clip.get(tick);
            if (s > c) {
                return c;
            } else if (s < -c) {
                return -c;
            }
            return s;
        }

        /**
         * 1: signal
         * 2: maximum value
         */
        private Clipper(Signal signal, Signal clip) {
            this.signal = signal;
            this.clip   = clip;
        }

    }

    public class ConstantClipper implements Signal {

        private Signal signal;
        private double clipPositive;
        private double clipNegative;
        
        public double get(long tick) {
            double s = signal.get(tick);
            if (s > clipPositive) {
                return clipPositive;
            } else if (s < clipNegative) {
                return clipNegative;
            }
            return s;
        }

        /**
         * 1: signal
         * 2: maximum value
         */
        private ConstantClipper(Signal signal, Signal clip) {
            this.signal = signal;
            this.clipPositive = clip.get(0);
            this.clipNegative = -clipPositive;
        }

    }
}
