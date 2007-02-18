package de.cgarbs.apsynth.signal;

import de.cgarbs.apsynth.internal.ClassType;


public abstract class SignalClass extends ClassType {
	
    abstract public Signal instantiate(Signal[] s);

    public String getType() {
        return "SignalClass";
    }
    
}
