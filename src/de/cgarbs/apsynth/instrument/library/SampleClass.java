package de.cgarbs.apsynth.instrument.library;

import de.cgarbs.apsynth.instrument.Instrument;
import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.note.Note;
import de.cgarbs.apsynth.signal.Signal;

// TODO use something better than all this fiddling with setSampleName()

public class SampleClass extends DefaultInstrumentClass {

    public SampleClass() {
        this.paramCount = 0;
    }
    
    public String getName() {
        return "Sample";
    }

    public Instrument instanciate(Signal[] s) {
        checkParams(s);
        return new Sample();
    }

    public static class Sample extends Instrument {

        // private Envelope env = new ADSREnvelope(0, 0, 1, 100);
        private String sampleName = null;
    	
        private Sample() {
        };
        
        public void setSample(String sampleName) {
        	this.sampleName = sampleName;
        }
        
        public Note play(Signal freq, long length) {
        	de.cgarbs.apsynth.signal.library.SampleClass.Sample sample =
        			(de.cgarbs.apsynth.signal.library.SampleClass.Sample) Pool.getSignalClass("Sample").instantiate(new Signal[]{});
        	sample.setSample(sampleName);
            return new Note(
            		sample,
                    length
                    );
        }
        
    }
}
