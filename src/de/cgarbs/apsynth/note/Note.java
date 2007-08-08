package de.cgarbs.apsynth.note;

import de.cgarbs.apsynth.signal.Signal;

public abstract class Note implements Signal {

    protected Signal signal = null;
    protected long duration = 0;
    protected long localTick = 0;

    public Note(Signal signal, long duration) {
        this.signal = signal;
        this.duration = duration;
    }

    abstract public double get(long tick, long local);
    
    abstract public boolean isFinished();

    /**
     * 1: signal
     * 2: duration (const)
     */
    public void setParameters(Signal[] s) {
        this.signal = s[0];
        this.duration = (long)s[0].get(0, 0);
    }

    abstract public boolean isEnveloped();
}
