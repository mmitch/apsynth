package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass.ConstantSignal;

public class SawtoothWaveClass extends DefaultSignalClass {

	// TODO write TriangleWaveClass
	
    /**
     * 1: frequency
     */
	public SawtoothWaveClass() {
		this.paramCount = 1;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
        if (s[0] instanceof ConstantSignal) {
            return new ConstantSawtoothWave(s[0]);
        }
		return new SawtoothWave(s[0]);
	}
	
	public String getName() {
		return "SawtoothWave";
	}

    public class SawtoothWave implements Signal {

        private Signal frequency = null;
      //private double lastFreq = 0;
        private double shift = 0;

        public double get(long tick) {

            // TODO implement bandwith limited construction
            // using sine waves
            // see http://en.wikipedia.org/wiki/Saw_wave
            
            double freq = frequency.get(tick);
            
            /*
             * TODO implement soft frequency change
            if (lastFreq != freq) {
                if (tick > 0) {
                    shift = (lastFreq / freq) * (tick + shift - 1) - (tick - 1);
                }
                lastFreq = freq;
            }
             */
            
            double x = (tick+shift) / 44100 * freq; 
            return (x - Math.floor(x))*2-1;

        }

        private SawtoothWave(Signal frequency) {
            this.frequency = frequency;
            
        }

    }

    public class ConstantSawtoothWave implements Signal {

        private double factor = 0;

        public double get(long tick) {

            double x = tick * factor; 
            return (x - Math.floor(x))*2-1;

        }

        private ConstantSawtoothWave(Signal frequency) {
            this.factor = frequency.get(0) / 44100;
            
        }

    }
}
