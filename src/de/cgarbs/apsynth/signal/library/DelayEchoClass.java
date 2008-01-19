package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.Apsynth;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;

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

    public static class DelayEcho implements Signal {

        private Signal signal = null;
        private int delay = 0;
        private Stereo ringBuffer[] = null;
        private Signal amp = null;
        private Signal reamp = null;
        private int pos = 0;
        
        
        private DelayEcho(Signal signal, Signal delay, Signal amp, Signal reamp) {
            this.signal = signal;
            this.delay = (int)(delay.get(0, 0).getMono()*Apsynth.samplefreq/1000);
            this.amp = amp;
            this.reamp = reamp;
            this.ringBuffer = new Stereo[this.delay];
            for (int i = 0; i < this.delay; i++) {
            	ringBuffer[i] = new Stereo();
            }
        }

        public Stereo get(long tick, long local) {
            Stereo original = signal.get(tick, local);
            Stereo processed = new Stereo(
            		original.mix(
            				new Stereo(ringBuffer[pos].l * amp.get(tick, local).l,
            						   ringBuffer[pos].r * amp.get(tick, local).r)
            				)
            				);

            ringBuffer[pos] = new Stereo(
            		original.mix(
            				new Stereo(ringBuffer[pos].l * reamp.get(tick, local).l,
            						   ringBuffer[pos].r * reamp.get(tick, local).r)
            				)
            				);
            
            pos++;
            if (pos==delay) {
                pos=0;
            }
            return processed;
        }

        public boolean isEnveloped() {
            return true;
        }

    }
}
