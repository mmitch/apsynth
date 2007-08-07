package de.cgarbs.apsynth.instrument.library;

import de.cgarbs.apsynth.instrument.Instrument;
import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.note.Note;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.ADSREnvelopeClass.ADSREnvelope;

public class SquareClass extends DefaultInstrumentClass {

    public SquareClass() {
        this.paramCount = 1;
    }
    
    public String getName() {
        return "Square";
    }

    public Instrument instanciate(Signal[] s) {
        checkParams(s);
        return new Square(s[0]);
    }

    public static class Square extends Instrument {

        private Signal duty = null;
        
        private Square(Signal duty) {
            this.duty = duty;
        }
        
        public Note play(Signal freq, long length) {
            return new Note(
                    Pool.getSignalClass("Amplifier").instantiate(new Signal[]{
                            Pool.getSignalClass("SquareWave").instantiate(new Signal[]{freq, duty}),
                            new ADSREnvelope(length, 0, 0, 1, 100)   
                    }),
                    length
                    );
        }
        
    }
}
