package de.cgarbs.apsynth.signal.library;

import java.util.HashMap;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;

// TODO unneeded?
public class ConstantSignalClass extends DefaultSignalClass {

    static HashMap<Stereo,ConstantSignal> cache = new HashMap<Stereo,ConstantSignal>(); 

    public ConstantSignalClass() {
		this.paramCount = 1;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
		// ugly, but the class has to wrap itself somehow...
		return instanciate(s[0].get(0, 0));
	}
	
	public Signal instanciate(Stereo value) {
        ConstantSignal retVal = cache.get(value);
        if (retVal == null) {
            retVal = new ConstantSignal(value);
            cache.put(value, retVal);
        }
        return retVal;
	}

	public String getName() {
		return "Constant";
	}
    
    private static ConstantSignalClass csc = new ConstantSignalClass();
    
    public static Signal get(Signal[] s) {
        return csc.instantiate(s);
    }

    public static Signal get(Stereo value) {
        return csc.instanciate(value);
    }

    public static Signal get(double value) {
        return csc.instanciate(new Stereo(value));
    }

    public static Signal get(double l, double r) {
        return csc.instanciate(new Stereo(l, r));
    }

    public static class ConstantSignal implements Signal {
        
        private Stereo value;

        private ConstantSignal(Stereo value) {
            this.value = value;
        };
        
        public Stereo get(long t, long l) {
            return value;
        }

        public boolean isEnveloped() {
            return false;
        }

    }
}
