package de.cgarbs.apsynth.instrument.library;

import de.cgarbs.apsynth.ADSREnvelope;
import de.cgarbs.apsynth.Envelope;
import de.cgarbs.apsynth.EnvelopeNote;
import de.cgarbs.apsynth.Note;
import de.cgarbs.apsynth.instrument.Instrument;
import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.signal.Signal;

public class HiHatClass extends DefaultInstrumentClass {

    public HiHatClass() {
        this.paramCount = 0;
    }
    
    public String getName() {
        return "HiHat";
    }

    public Instrument instanciate(Signal[] s) {
        checkParams(s);
        return new HiHat();
    }

    public static class HiHat extends Instrument {

        private Envelope env = new ADSREnvelope(10, 7000, 0, 300);
        
        private HiHat() {
        };
        
        public Note play(Signal freq, long length) {
            return new EnvelopeNote(
                    Pool.getSignalClass("WhiteNoise").instantiate(new Signal[]{}),
                    length,
                    env
                    );
        }
        
    }
}
