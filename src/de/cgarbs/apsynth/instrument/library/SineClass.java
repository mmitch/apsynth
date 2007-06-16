package de.cgarbs.apsynth.instrument.library;

import de.cgarbs.apsynth.EnvelopeNote;
import de.cgarbs.apsynth.Note;
import de.cgarbs.apsynth.envelope.ADSREnvelope;
import de.cgarbs.apsynth.envelope.Envelope;
import de.cgarbs.apsynth.instrument.Instrument;
import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.signal.Signal;

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

        private Envelope env = new ADSREnvelope(0, 0, 1, 100);
        
        private Square() {

        }
        
        public Note play(Signal freq, long length) {
            return new EnvelopeNote(
                    Pool.getSignalClass("SineWave").instantiate(new Signal[]{freq}),
                            length,
                            env
                            );
        }
        
    }
}
