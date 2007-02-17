package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.Apsynth;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass.ConstantSignal;

public class SineWaveClass extends DefaultSignalClass {

    /**
     * 1: frequency
     */
    public SineWaveClass() {
		this.paramCount = 1;
	}
	
	public Signal instanciate(Signal[] s) {
		checkParams(s);
        if (s[0] instanceof ConstantSignal) {
            return new ConstantSineWave(s[0]);
        }
        return new SineWave(s[0]);
	}
	
	public String getName() {
		return "SineWave";
	}

    public class SineWave implements Signal {

        private Signal frequency = null;
        private double lastFreq = 0;
        private double shift = 0;

        public double get(long tick) {

            double freq = frequency.get(tick);
            
            if (lastFreq != freq) {
                if (tick > 0) {
                    shift = (lastFreq / freq) * (tick + shift - 1) - (tick - 1);
                }
                lastFreq = freq;
            }
            
            return Math.sin((tick+shift) * 2*Math.PI / Apsynth.samplefreq * freq);

        }

        private SineWave(Signal frequency) {
            this.frequency = frequency;
        }

    }

    public class ConstantSineWave implements Signal {

        private double factor = 0;

        public double get(long tick) {
            return Math.sin(tick * factor);
        }

        private ConstantSineWave(Signal frequency) {
            this.factor = frequency.get(0) * 2*Math.PI / Apsynth.samplefreq;
        }

    }
}
