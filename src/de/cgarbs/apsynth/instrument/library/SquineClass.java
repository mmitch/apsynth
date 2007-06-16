package de.cgarbs.apsynth.instrument.library;

import de.cgarbs.apsynth.envelope.ADSREnvelope;
import de.cgarbs.apsynth.envelope.Envelope;
import de.cgarbs.apsynth.instrument.Instrument;
import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.note.EnvelopeNote;
import de.cgarbs.apsynth.note.Note;
import de.cgarbs.apsynth.signal.Signal;

public class SquineClass extends DefaultInstrumentClass {

    public SquineClass() {
        this.paramCount = 1;
    }
    
    public String getName() {
        return "Squine";
    }

    public Instrument instanciate(Signal[] s) {
        checkParams(s);
        return new Square(s[0]);
    }

    public static class Square extends Instrument {

        private Envelope env = new ADSREnvelope(0, 0, 1, 100);
        private Signal sound = null;
        
        private Square(Signal sound) {
            this.sound = sound;
        }
        
        public Note play(Signal freq, long length) {
            return new EnvelopeNote(
                    Pool.getSignalClass("SquineWave").instantiate(new Signal[]{freq, sound}),
                            length,
                            env
                            );
        }
        
    }
}
