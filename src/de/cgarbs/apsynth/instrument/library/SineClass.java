package de.cgarbs.apsynth.instrument.library;

import de.cgarbs.apsynth.instrument.Instrument;
import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.note.EnvelopeNote;
import de.cgarbs.apsynth.note.Note;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.ADSREnvelopeClass.ADSREnvelope;

public class SineClass extends DefaultInstrumentClass {

    public SineClass() {
        this.paramCount = 0;
    }
    
    public String getName() {
        return "Sine";
    }

    public Instrument instanciate(Signal[] s) {
        checkParams(s);
        return new Square();
    }

    public static class Square extends Instrument {

        private Square() {

        }
        
        public Note play(Signal freq, long length) {
            return new EnvelopeNote(
                    Pool.getSignalClass("Amplifier").instantiate(new Signal[]{
                            Pool.getSignalClass("SineWave").instantiate(new Signal[]{freq}),
                            new ADSREnvelope(length, 0, 0, 1, 100)   
                    }),
                    length
                    );
        }
        
    }
}
