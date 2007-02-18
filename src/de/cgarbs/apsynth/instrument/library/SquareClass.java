package de.cgarbs.apsynth.instrument.library;

import de.cgarbs.apsynth.ADSREnvelope;
import de.cgarbs.apsynth.Envelope;
import de.cgarbs.apsynth.EnvelopeNote;
import de.cgarbs.apsynth.Note;
import de.cgarbs.apsynth.instrument.DefaultInstrumentClass;
import de.cgarbs.apsynth.instrument.Instrument;
import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.signal.Signal;

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

    public class Square extends Instrument {

        private Envelope env = new ADSREnvelope(0, 0, 1, 100);
        private Signal duty = null;
        
        private Square(Signal duty) {
            this.duty = duty;
        }
        
        public Note play(Signal freq, long length) {
            return new EnvelopeNote(
                    Pool.getSignalClass("SquareWave").instantiate(new Signal[]{freq, duty}),
                            length,
                            env
                            );
        }
        
    }
}
