package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;

public class SquareWaveClass extends DefaultSignalClass {

    // TODO constant optimization
    
    /**
     * 1: frequency
     * 2: duty (0..1)
     */
 	public SquareWaveClass() {
		this.paramCount = 2;
	}
	
	public Signal instanciate(Signal[] s) {
		checkParams(s);
		return new SquareWave(s[0], s[1]);
	}
	
	public String getName() {
		return "SquareWave";
	}

    public class SquareWave implements Signal {

        private Signal frequency = null;
        private Signal duty = null;  // 0..1
      //private double lastFreq = 0;
        private double shift = 0;
        

        public double get(long tick) {

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
            return (x - Math.floor(x) > duty.get(tick)) ? 1 : -1;

        }

        private SquareWave(Signal frequency, Signal duty) {
            this.frequency = frequency;
            this.duty = duty;
        }

    }
}
