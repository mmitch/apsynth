package de.cgarbs.apsynth.note;

import de.cgarbs.apsynth.signal.Signal;

public class Note implements Signal {

    Signal signal = null;
    long duration = 0;
    long localTick = 0;

    public Note(Signal signal, long duration) {
        this.signal = signal;
        this.duration = duration;
    }

    public double get(long tick, long local) {
    	// TODO hier localTick oder nicht benutzen=
        //return signal.get(localTick++);
        return signal.get(tick, localTick++);
    }
    
    public boolean isFinished(long tick) {
        return (localTick >= duration);
    }

    /**
     * 1: signal
     * 2: duration (const)
     */
    public void setParameters(Signal[] s) {
        this.signal = s[0];
        this.duration = (long)s[0].get(0, 0);
    }
    
}
