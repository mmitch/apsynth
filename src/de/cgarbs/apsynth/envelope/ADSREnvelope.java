package de.cgarbs.apsynth.envelope;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass;

public class ADSREnvelope implements Envelope {

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
        
        if (localTick > duration + rtime.get(tick)) {
            return 0;
        }
        if (localTick > duration) {
            return slevel.get(tick) * (1-((localTick - duration)/rtime.get(tick)));
        }
        if (localTick >= atime.get(tick)+dtime.get(tick)) {
            return slevel.get(tick);
        }
        if (localTick > atime.get(tick)) {
            return 1 - slevel.get(tick) * ((localTick - atime.get(tick))/dtime.get(tick));
        }
        return localTick/atime.get(tick);
    }

    public boolean isFinished(long tick, long localTick, long duration) {
        
        // TODO: if slevel == 0, localTick > atime + dtime is enough
        
        if (localTick > duration + rtime.get(tick)) {
            return true;
        }
        return false;
    }

}
