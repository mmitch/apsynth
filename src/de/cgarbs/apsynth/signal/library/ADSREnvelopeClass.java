package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;

public class ADSREnvelopeClass extends DefaultSignalClass {

    /**
     * 1: note duration
     * 2: attack time
     * 3: decay time
     * 4: sustain level
     * 5: release time
     */
    public ADSREnvelopeClass() {
        this.paramCount = 5;
    }
    
    public Signal instantiate(Signal[] s) {
        checkParams(s);
        return new ADSREnvelope(s[0], s[1], s[2], s[3], s[4]);
    }

    public String getName() {
        return "ADSREnvelope";
    }
    
    public static class ADSREnvelope implements Signal {

        
        // TODO use an EnvelopeClass to wrap envelopes
        // TODO constant optimization 
        
        Signal atime  = null;
        Signal dtime  = null;
        Signal slevel = null;
        Signal rtime  = null;
        Signal duration = null;
        
        public ADSREnvelope(Signal duration, Signal atime, Signal dtime, Signal slevel, Signal rtime) {
            this.duration = duration;
            this.atime    = atime;
            this.dtime    = dtime;
            this.slevel   = slevel;
            this.rtime    = rtime;
        }
        
        public ADSREnvelope(double duration, double atime, double dtime, double slevel, double rtime) {
            this(
                    ConstantSignalClass.get(duration),
                    ConstantSignalClass.get(atime),
                    ConstantSignalClass.get(dtime),
                    ConstantSignalClass.get(slevel),
                    ConstantSignalClass.get(rtime)
                );
        }
        
        public double get(long tick, long localTick) {
            
            // TODO: implement more natural exponential decay
            // see: http://en.wikipedia.org/wiki/Exponential_decay
            //      http://en.wikipedia.org/wiki/Synthesizer
            //      http://en.wikipedia.org/wiki/ADSR
            
            if (localTick > duration.get(tick, localTick) + rtime.get(tick, localTick)) {
                return 0;
            }
            if (localTick > duration.get(tick, localTick)) {
                return slevel.get(tick, localTick) * (1-((localTick - duration.get(tick, localTick))/rtime.get(tick, localTick)));
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
