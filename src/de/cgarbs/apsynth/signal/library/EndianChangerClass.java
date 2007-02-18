package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;

public class EndianChangerClass extends DefaultSignalClass {

    public EndianChangerClass() {
		this.paramCount = 1;
	}
	
    /**
     * 1: signal
     */
	public Signal instantiate(Signal[] s) {
		checkParams(s);
		return new EndianChanger(s[0]);
	}
	
	public String getName() {
		return "EndianChanger";
	}

    public class EndianChanger implements Signal {

        private Signal signal = null;

        public double get(long tick) {

            int value = (int) ((signal.get(tick)+1) * 32767);
            byte low = (byte) (value % 256);
            byte hi  = (byte) (value / 256);
            return ((double)(low*256+hi))/65535;

        }

        private EndianChanger(Signal signal) {
            this.signal = signal;
        }

    }
}
