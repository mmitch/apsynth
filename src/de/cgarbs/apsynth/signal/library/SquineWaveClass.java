package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass.ConstantSignal;

public class SquineWaveClass extends DefaultSignalClass {

    /**
     * 1: frequency
     * 2: sound ]0==square..1==sine]
     */
    public SquineWaveClass() {
		this.paramCount = 2;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
        if (s[1] instanceof ConstantSignal) {
            return new ConstantSquineWave(s[0], s[1]);
        }
        return new SquineWave(s[0], s[1]);
	}
	
	public String getName() {
		return "SquineWave";
	}

    public static class SquineWave implements Signal {

        private Signal clipped = null;
        private Signal sound = null;
        private boolean enveloped;
        
        public Stereo get(long tick, long local) {
            Stereo normalize = sound.get(tick, local);
            if (normalize.l != 0) {
                normalize.l = 1/normalize.l;
            }
            if (normalize.r != 0) {
                normalize.r = 1/normalize.r;
            }
            Stereo clip = clipped.get(tick, local);
            return new Stereo(clip.l * normalize.l, clip.r * normalize.r);
        }

        private SquineWave(Signal frequency, Signal sound) {
            Signal sine  = Pool.getSignalClass("SineWave").instantiate(new Signal[]{frequency});
            this.clipped = Pool.getSignalClass("Clipper").instantiate(new Signal[]{sine, sound});
            this.sound   = sound;

            enveloped = clipped.isEnveloped();
        }

        public boolean isEnveloped() {
            return enveloped;
        }

    }

    public class ConstantSquineWave implements Signal {

        private Signal clipped = null;
        private Stereo normalize;
        private boolean enveloped = false;
        
        public Stereo get(long tick, long local) {
            Stereo clip = clipped.get(tick, local);
            return new Stereo(clip.l * normalize.l, clip.r * normalize.r);
        }

        private ConstantSquineWave(Signal frequency, Signal sound) {
            Signal sine  = Pool.getSignalClass("SineWave").instantiate(new Signal[]{frequency});
            this.clipped = Pool.getSignalClass("Clipper").instantiate(new Signal[]{sine, sound});
            this.normalize = sound.get(0, 0);
            if (normalize.l != 0) {
                normalize.l = 1/normalize.l;
            }
            if (normalize.r != 0) {
                normalize.r = 1/normalize.r;
            }

            enveloped = clipped.isEnveloped();
        }

        public boolean isEnveloped() {
            return enveloped;
        }

    }
}
