package de.cgarbs.apsynth.instrument.library;

import de.cgarbs.apsynth.envelope.Envelope;
import de.cgarbs.apsynth.envelope.ADSREnvelopeClass.ADSREnvelope;
import de.cgarbs.apsynth.instrument.Instrument;
import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.note.EnvelopeNote;
import de.cgarbs.apsynth.note.Note;
import de.cgarbs.apsynth.signal.Signal;

public class SawtoothClass extends DefaultInstrumentClass {

    public SawtoothClass() {
        this.paramCount = 0;
    }
    
    public String getName() {
        return "Sawtooth";
    }

    public Instrument instanciate(Signal[] s) {
        checkParams(s);
        return new Sawtooth();
    }

    public static class Sawtooth extends Instrument {

        private Envelope env = new ADSREnvelope(0, 0, 1, 100);
        
        private Sawtooth() {
            
        };
        
        public Note play(Signal freq, long length) {
            return new EnvelopeNote(
                    Pool.getSignalClass("SawtoothWave").instantiate(new Signal[]{freq}),
                    length,
                    env
                    );
        }
        
    }
}
