package de.cgarbs.apsynth.internal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import de.cgarbs.apsynth.Sample;
import de.cgarbs.apsynth.Track;
import de.cgarbs.apsynth.envelope.EnvelopeClass;
import de.cgarbs.apsynth.instrument.InstrumentClass;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.SignalClass;
import de.cgarbs.apsynth.signal.library.RegisterClass.Register;
import de.cgarbs.apsynth.storage.FilesystemStorage;

public class Pool {

    static HashMap<String,SignalClass> signalClassPool = new HashMap<String,SignalClass>();
    static HashMap<String,InstrumentClass> instrumentClassPool = new HashMap<String,InstrumentClass>();
    static HashMap<String,EnvelopeClass> envelopeClassPool = new HashMap<String,EnvelopeClass>();
    static HashMap<String,Sample> samplePool = new HashMap<String,Sample>();
    static HashMap<String,Register> registerPool = new HashMap<String,Register>();
    static Vector<Track> trackPool = new Vector<Track>();
	
    public static Sample getSample(String name) {
    	Sample sample = samplePool.get(name); 
    	if (sample == null) {
    		sample = new FilesystemStorage().readSample(name);
    		samplePool.put(name, sample);
    	}
    	return sample;
    }
    
    public static void registerTrack(Track t) {
        trackPool.add(t);
    }
    
    public static boolean allTracksFinished() {
        Iterator<Track> i = trackPool.iterator();
        while (i.hasNext()) {
            if (! i.next().isFinished()) {
                return false;
            }
        }
        return true;
    }
    
    public static void registerSignalClass(SignalClass sc) {

        if ( signalClassPool.get(sc.getName()) != null) {
            // TODO add proper exception
            throw new RuntimeException("SignalClass "+sc.getName()+" registered twice at ClassPool");
        }
        signalClassPool.put(sc.getName(), sc);
        
    }
    
    public static void registerEnvelopeClass(EnvelopeClass ec) {

        if ( envelopeClassPool.get(ec.getName()) != null) {
            // TODO add proper exception
            throw new RuntimeException("EnvelopeClass "+ec.getName()+" registered twice at ClassPool");
        }
        envelopeClassPool.put(ec.getName(), ec);
        
    }
    
    public static SignalClass getSignalClass(String name) {
        SignalClass retVal = signalClassPool.get(name);
        if (retVal == null) {
            // TODO add proper exception
            throw new RuntimeException("SignalClass "+name+" not registered in ClassPool");
        }
        return retVal;
        
    }

    public static EnvelopeClass getEnvelopeClass(String name) {
        EnvelopeClass retVal = envelopeClassPool.get(name);
        if (retVal == null) {
            // TODO add proper exception
            throw new RuntimeException("EnvelopeClass "+name+" not registered in ClassPool");
        }
        return retVal;
        
    }

    public static void registerInstrumentClass(InstrumentClass ic) {

        if ( instrumentClassPool.get(ic.getName()) != null) {
            // TODO add proper exception
            throw new RuntimeException("InstrumentClass "+ic.getName()+" registered twice at ClassPool");
        }
        instrumentClassPool.put(ic.getName(), ic);
        
    }
    
    public static InstrumentClass getInstrumentClass(String name) {
        InstrumentClass retVal = instrumentClassPool.get(name);
        if (retVal == null) {
            // TODO add proper exception
            throw new RuntimeException("InstrumentClass "+name+" not registered in ClassPool");
        }
        return retVal;
        
    }
    
    public static Register getRegister(String name) {
        Register retVal = registerPool.get(name);
        if (retVal == null) {
            retVal = (Register) getSignalClass("Register").instantiate(new Signal[0]);
            registerRegister(name, retVal);
        }
        return retVal;
    }
    
    public static void registerRegister(String name, Register r) {
        if ( registerPool.get(name) != null) {
            // TODO add proper exception
            throw new RuntimeException("Register "+name+" registered twice at ClassPool");
        }
        registerPool.put(name, r);
    }
}
