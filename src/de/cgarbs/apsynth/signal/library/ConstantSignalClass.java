package de.cgarbs.apsynth.signal.library;

import java.util.HashMap;

import de.cgarbs.apsynth.signal.Signal;

// TODO unneeded?
public class ConstantSignalClass extends DefaultSignalClass {

    static HashMap<Double,ConstantSignal> cache = new HashMap<Double,ConstantSignal>(); 

    public ConstantSignalClass() {
		this.paramCount = 1;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
		// ugly, but the class has to wrap itself somehow...
		return instanciate(s[0].get(0, 0));
	}
	
	public Signal instanciate(double value) {
        Double key = new Double(value);
        ConstantSignal retVal = cache.get(key);
        if (retVal == null) {
            retVal = new ConstantSignal(value);
            cache.put(key, retVal);
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

    public static Signal get(double value) {
        return csc.instanciate(value);
    }

    public static class ConstantSignal implements Signal {
        
        private double value = 0;

        private ConstantSignal(double value) {
            this.value = value;
        };
        
        public double get(long t, long l) {
            return value;
        }

        public boolean isEnveloped() {
            return false;
        }

    }
}
