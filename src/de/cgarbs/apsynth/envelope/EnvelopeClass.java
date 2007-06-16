package de.cgarbs.apsynth.envelope;

import de.cgarbs.apsynth.internal.ClassType;
import de.cgarbs.apsynth.signal.Signal;

public abstract class EnvelopeClass extends ClassType {

    abstract public Envelope instanciate(Signal[] s);

    public String getType() {
        return "EnvelopeClass";
    }

}
