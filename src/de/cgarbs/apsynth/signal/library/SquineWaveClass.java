package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.signal.Signal;
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
        
        public double get(long tick) {
            double normalize = sound.get(tick);
            if (normalize != 0) {
                normalize = 1/normalize;
            }
            return clipped.get(tick) * normalize;
        }

        private SquineWave(Signal frequency, Signal sound) {
            Signal sine  = Pool.getSignalClass("SineWave").instantiate(new Signal[]{frequency});
            this.clipped = Pool.getSignalClass("Clipper").instantiate(new Signal[]{sine, sound});
            this.sound   = sound;
        }

    }

    public class ConstantSquineWave implements Signal {

        private Signal clipped = null;
        private double normalize = 0;
        
        public double get(long tick) {
            return clipped.get(tick) * normalize;
        }

        private ConstantSquineWave(Signal frequency, Signal sound) {
            Signal sine  = Pool.getSignalClass("SineWave").instantiate(new Signal[]{frequency});
            this.clipped = Pool.getSignalClass("Clipper").instantiate(new Signal[]{sine, sound});
            this.normalize = sound.get(0);
            if (normalize!= 0) {
                normalize = 1/normalize;
            }
        }

    }
}
