package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.internal.IntervalArray;
import de.cgarbs.apsynth.internal.IntervalArray1;
import de.cgarbs.apsynth.signal.Signal;

public class RegisterClass extends DefaultSignalClass {

	public RegisterClass() {
		this.paramCount = 0;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
		return new Register();
	}
	
	public String getName() {
		return "Register";
	}

    public static class Register implements Signal {

        IntervalArray ary = new IntervalArray1();
        
        private Register () {
            // no-op: no parameters
            ary.put(0, 0);
        }

        public double get(long tick, long local) {
            return ary.get(tick);
        }
        
        public void set(long tick, double value) {
            ary.put(tick, value);
        }

        public boolean isEnveloped() {
            return false;
        }
    }
    
    public static boolean isRegister(String token) {
        return token.startsWith("R_");
    }
}
