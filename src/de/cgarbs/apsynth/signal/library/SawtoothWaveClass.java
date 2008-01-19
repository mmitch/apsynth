package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.Apsynth;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;
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

    public static class SawtoothWave implements Signal {

        private Signal frequency = null;
      //private double lastFreq = 0;
        private double shift = 0;

        public Stereo get(long tick, long local) {

            // TODO implement bandwith limited construction
            // using sine waves
            // see http://en.wikipedia.org/wiki/Saw_wave
            
            double freq = frequency.get(tick, local).getMono();
            
            /*
             * TODO implement soft frequency change
            if (lastFreq != freq) {
                if (tick > 0) {
                    shift = (lastFreq / freq) * (tick + shift - 1) - (tick - 1);
                }
                lastFreq = freq;
            }
             */
            
            double x = (tick+shift) / Apsynth.samplefreq * freq; 
            return new Stereo((x - Math.floor(x))*2-1);

        }

        private SawtoothWave(Signal frequency) {
            this.frequency = frequency;
            
        }

        public boolean isEnveloped() {
            return false;
        }

    }

    public class ConstantSawtoothWave implements Signal {

        private double factor = 0;

        public Stereo get(long tick, long local) {

            double x = tick * factor; 
            return new Stereo((x - Math.floor(x))*2-1);

        }

        private ConstantSawtoothWave(Signal frequency) {
            this.factor = frequency.get(0, 0).getMono() / Apsynth.samplefreq;
            
        }

        public boolean isEnveloped() {
            return false;
        }

    }
}
