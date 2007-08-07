package de.cgarbs.apsynth.envelope;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass;

public class ADSREnvelopeClass extends EnvelopeClass {

    /**
     * 1: attack time
     * 2: decay time
     * 3: sustain level
     * 4: release time
     */
    public ADSREnvelopeClass() {
        this.paramCount = 4;
    }
    
    public Envelope instanciate(Signal[] s) {
        checkParams(s);
        return new ADSREnvelope(s[0], s[1], s[2], s[3]);
    }

    public String getName() {
        return "ADSREnvelope";
    }
    
    public static class ADSREnvelope implements Envelope {

        
        // TODO use an EnvelopeClass to wrap envelopes
        // TODO constant optimization 
        
        Signal atime  = null;
        Signal dtime  = null;
        Signal slevel = null;
        Signal rtime  = null;
        
        public ADSREnvelope(Signal atime, Signal dtime, Signal slevel, Signal rtime) {
            this.atime  = atime;
            this.dtime  = dtime;
            this.slevel = slevel;
            this.rtime  = rtime;
        }
        
        public ADSREnvelope(double atime, double dtime, double slevel, double rtime) {
            this(
                    ConstantSignalClass.get(atime),
                    ConstantSignalClass.get(dtime),
                    ConstantSignalClass.get(slevel),
                    ConstantSignalClass.get(rtime)
                );
        }
        
        public double get(long tick, long localTick, long duration) {
            
            // TODO: implement more natural exponential decay
            // see: http://en.wikipedia.org/wiki/Exponential_decay
            //      http://en.wikipedia.org/wiki/Synthesizer
            //      http://en.wikipedia.org/wiki/ADSR
            
            if (localTick > duration + rtime.get(tick, localTick)) {
                return 0;
            }
            if (localTick > duration) {
                return slevel.get(tick, localTick) * (1-((localTick - duration)/rtime.get(tick, localTick)));
            }
            if (localTick >= atime.get(tick, localTick)+dtime.get(tick, localTick)) {
                return slevel.get(tick, localTick);
            }
            if (localTick > atime.get(tick, localTick)) {
                return 1 - slevel.get(tick, localTick) * ((localTick - atime.get(tick, localTick))/dtime.get(tick, localTick));
            }
            return localTick/atime.get(tick, localTick);
        }
    
        public boolean isFinished(long tick, long localTick, long duration) {
            
            // TODO: if slevel == 0, localTick > atime + dtime is enough
            
            if (localTick > duration + rtime.get(tick, localTick)) {
                return true;
            }
            return false;
        }
    
    }

}
