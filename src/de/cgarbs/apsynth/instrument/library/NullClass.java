package de.cgarbs.apsynth.instrument.library;

import de.cgarbs.apsynth.instrument.Instrument;
import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.note.Note;
import de.cgarbs.apsynth.signal.Signal;

public class NullClass extends DefaultInstrumentClass {

    public NullClass() {
        this.paramCount = 0;
    }
    
    public String getName() {
        return "Null";
    }

    public Instrument instanciate(Signal[] s) {
        checkParams(s);
        return new Null();
    }

    public static class Null extends Instrument {

        private Null() {
            // no-op: no parameters 
        }
        
        public Note play(Signal freq, long length) {
            return new Note(
                    Pool.getSignalClass("Null").instantiate(new Signal[]{}),
                    0
                            );
        }
        
    }
}
