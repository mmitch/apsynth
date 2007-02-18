package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.Apsynth;
import de.cgarbs.apsynth.signal.Signal;

public class DelayEchoClass extends DefaultSignalClass {

    // TODO constant optimization
    
	public DelayEchoClass() {
		this.paramCount = 4;
	}
	
    /**
     * 1: signal
     * 2: delay buffer size [ms] (const)
     * 3: amplification
     * 4: reamplification 
     */    
	public Signal instantiate(Signal[] s) {
		checkParams(s);
		return new DelayEcho(s[0], s[1], s[2], s[3]);
	}
	
	public String getName() {
		return "DelayEcho";
	}

    public class DelayEcho implements Signal {

        private Signal signal = null;
        private int delay = 0;
        private double ringBuffer[] = null;
        private Signal amp = null;
        private Signal reamp = null;
        private int pos = 0;
        
        
        private DelayEcho(Signal signal, Signal delay, Signal amp, Signal reamp) {
            this.signal = signal;
            this.delay = (int)(delay.get(0)*Apsynth.samplefreq/1000);
            this.amp = amp;
            this.reamp = reamp;
            this.ringBuffer = new double[this.delay];
        }

        public double get(long tick) {
            double original = signal.get(tick);
            double processed = original + (ringBuffer[pos] * amp.get(tick));
            ringBuffer[pos] = original + (ringBuffer[pos] * reamp.get(tick));
            pos++;
            if (pos==delay) {
                pos=0;
            }
            return processed;
        }

    }
}
