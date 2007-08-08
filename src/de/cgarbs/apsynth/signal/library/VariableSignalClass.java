package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.internal.IntervalArray;
import de.cgarbs.apsynth.internal.IntervalArray1;
import de.cgarbs.apsynth.signal.Signal;

public class VariableSignalClass extends DefaultSignalClass {


    public VariableSignalClass() {
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
        return new VariableSignal();
	}

	public String getName() {
		return "Variable";
	}

    public static class VariableSignal implements Signal {
        
        private IntervalArray val = new IntervalArray1();

        private VariableSignal() {
            val.put(0, 0);
        };
        
        public double get(long t, long l) {
            return val.get(t);
        }

        public void set(long time, double value) {
            val.put(time, value);
        }

        public boolean isEnveloped() {
            return false;
        }
        
    }
}
