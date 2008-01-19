package de.cgarbs.apsynth.note;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;

public class FixNote extends Note {

    public FixNote(Signal signal, long duration) {
        super(signal, duration);
    }

    public boolean isFinished() {
        return localTick >= duration;
    }

    public Stereo get(long tick, long local) {
        return signal.get(tick, localTick++);
    }

    public boolean isEnveloped() {
        return false;
    }
    
}
