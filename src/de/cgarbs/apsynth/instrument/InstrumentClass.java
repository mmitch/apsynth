package de.cgarbs.apsynth.instrument;

import de.cgarbs.apsynth.internal.ClassType;
import de.cgarbs.apsynth.signal.Signal;


public abstract class InstrumentClass extends ClassType {
	
	abstract public Instrument instanciate(Signal[] s);

    public String getType() {
        return "InstrumentClass";
    }
    
}
