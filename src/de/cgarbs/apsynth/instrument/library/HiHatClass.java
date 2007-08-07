package de.cgarbs.apsynth.instrument.library;

import de.cgarbs.apsynth.instrument.Instrument;
import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.note.Note;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.ADSREnvelopeClass.ADSREnvelope;

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

        private HiHat() {
        };
        
        public Note play(Signal freq, long length) {
            
            return new Note(
                    Pool.getSignalClass("Amplifier").instantiate(new Signal[]{
                            Pool.getSignalClass("WhiteNoise").instantiate(new Signal[]{}),
                            new ADSREnvelope(length, 10, 7000, 0, 300)   
                    }),
                    length
                    );
        }
        
    }
}
