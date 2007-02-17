package de.cgarbs.apsynth.signal;

import de.cgarbs.apsynth.internal.ClassType;


public abstract class SignalClass extends ClassType {
	
    abstract public Signal instanciate(Signal[] s);

    public String getType() {
        return "SignalClass";
    }
    
}
