package de.cgarbs.apsynth.signal.library;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;

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

    public static class EndianChanger implements Signal {

        private Signal signal = null;
        private boolean enveloped;

        public Stereo get(long tick, long local) {

            int valuel = (int) ((signal.get(tick, local).l+1) * 32767);
            byte hil  = (byte) (valuel / 256);
            byte lowl = (byte) (valuel % 256);

            int valuer = (int) ((signal.get(tick, local).r+1) * 32767);
            byte lowr = (byte) (valuer % 256);
            byte hir  = (byte) (valuer / 256);

            return new Stereo(((double)(lowl*256+hil))/65535,
            				  ((double)(lowr*256+hir))/65535);

        }

        private EndianChanger(Signal signal) {
            this.signal = signal;
            enveloped = signal.isEnveloped();
        }

        public boolean isEnveloped() {
            return enveloped;
        }

    }
}
