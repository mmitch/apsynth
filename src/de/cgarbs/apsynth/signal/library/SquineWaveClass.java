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
	
	public Signal instanciate(Signal[] s) {
		checkParams(s);
        if (s[1] instanceof ConstantSignal) {
            return new ConstantSquineWave(s[0], s[1]);
        }
        return new SquineWave(s[0], s[1]);
	}
	
	public String getName() {
		return "SquineWave";
	}

    public class SquineWave implements Signal {

        private Signal clipped = null;
        private Signal sound = null;
        
        public double get(long tick) {
            double div = sound.get(tick);
            if (div != 0) {
                div = 1/div;
            }
            return clipped.get(tick) * div;
        }

        private SquineWave(Signal frequency, Signal sound) {
            Signal sine  = Pool.getSignalClass("SineWave").instanciate(new Signal[]{frequency});
            this.clipped = Pool.getSignalClass("Clipper").instanciate(new Signal[]{sine, sound});
            this.sound   = sound;
        }

    }

    public class ConstantSquineWave implements Signal {

        private Signal clipped = null;
        private double div = 0;
        
        public double get(long tick) {
            return clipped.get(tick) * div;
        }

        private ConstantSquineWave(Signal frequency, Signal sound) {
            Signal sine  = Pool.getSignalClass("SineWave").instanciate(new Signal[]{frequency});
            this.clipped = Pool.getSignalClass("Clipper").instanciate(new Signal[]{sine, sound});
            this.div     = sound.get(0);
            if (div != 0) {
                div = 1/div;
            }
        }

    }
}
