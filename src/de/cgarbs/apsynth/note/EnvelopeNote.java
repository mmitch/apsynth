package de.cgarbs.apsynth.note;

import de.cgarbs.apsynth.envelope.Envelope;
import de.cgarbs.apsynth.signal.Signal;

public class EnvelopeNote extends Note {

    Envelope env = null;
    
    public EnvelopeNote(Signal signal, long duration, Envelope env) {
        super(signal, duration);
        this.env = env;
    }
    
    public double get(long tick, long local) {
    	// TODO hier local Tick oder nicht?
        //return env.get(tick, localTick, duration) * signal.get(localTick++);
        return env.get(tick, localTick, duration) * signal.get(tick, localTick++);
    }
    
    public boolean isFinished(long tick) {
        return env.isFinished(tick, localTick, duration);
    }
}
