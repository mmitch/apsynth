package de.cgarbs.apsynth.note;

import de.cgarbs.apsynth.Apsynth;
import de.cgarbs.apsynth.SilenceDetector;
import de.cgarbs.apsynth.signal.Signal;

public class EnvelopeNote extends Note {

    private SilenceDetector s = null;

    public EnvelopeNote(Signal signal, long duration) {
        super(signal, duration);
        s = new SilenceDetector(Apsynth.samplefreq/5);
    }

    public double get(long tick, long local) {
        return s.monitor(signal.get(tick, localTick++));
    }
    
    public boolean isFinished() {
        return (localTick >= duration && s.isSilent());
    }

    public boolean isEnveloped() {
        return true;
    }
    
}
