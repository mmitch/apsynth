package de.cgarbs.apsynth.instrument.dynamic;

import de.cgarbs.apsynth.instrument.Instrument;
import de.cgarbs.apsynth.instrument.InstrumentClass;
import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.note.Note;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.SignalClass;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass;

public class DynamicInstrumentClass extends InstrumentClass {

    private String name = "unnamedDynamicInstrumentClass";
    private String signal = "Null";
    
    public DynamicInstrumentClass(String name, String signal) {
        this.name = name;
        this.signal = signal;
    }
    
    public Instrument instanciate(Signal[] s) {
        return new DynamicInstrument(Pool.getSignalClass(signal), s);
    }

    public String getName() {
        return this.name;
    }
    
    @Override
    public int getParamCount() {
        return Pool.getSignalClass(signal).getParamCount() - 2;
    }



    public class DynamicInstrument extends Instrument {

        private SignalClass signalClass = null;
        private Signal[] s = {};
        
        private DynamicInstrument(SignalClass signalClass, Signal[] s) {
            this.signalClass = signalClass;
            this.s = s;
        }
        
        public Note play(Signal freq, long length) {
            
            Signal[] signals = new Signal[s.length + 2];
            signals[0] = freq;
            signals[1] = ConstantSignalClass.get(length);
            for (int i=0; i<s.length ;i++) {
                signals[i+2] = s[i];
            }
            
            return new Note(
                       signalClass.instantiate(signals),
                               length
                               );
        }
        
    }

}
