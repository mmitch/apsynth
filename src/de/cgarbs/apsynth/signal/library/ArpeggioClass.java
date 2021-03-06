package de.cgarbs.apsynth.signal.library;

import java.util.Vector;

import de.cgarbs.apsynth.Apsynth;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass.ConstantSignal;

public class ArpeggioClass extends DefaultSignalClass {

    /**
     * 1: length in milliseconds
     * 2..n: list of Signals
     *
     */
	public ArpeggioClass() {
		this.paramCount = 2;
	}
	
	public Signal instantiate(Signal[] s) {
        checkMinParams(s);
        if (s[0] instanceof ConstantSignal) {
            return new ConstantArpeggio(s);
        }
        return new Arpeggio(s);
	}
	
	public String getName() {
		return "Arpeggio";
	}

    public static class Arpeggio implements Signal {

        private long startTick = -1;
        private int current = 0;
        private Signal lengthInMs;
        private Vector<Signal> signals = new Vector<Signal>();
        private boolean enveloped;
        
        public double get(long t, long l) {
            if (startTick == -1) {
                startTick = t;
            }
            if (t > startTick+(lengthInMs.get(t, l) * Apsynth.samplefreq / 1000)) {
                startTick = t;
                current++;
                if (current == signals.size()) {
                    current = 0;
                }
            }
            return signals.get(current).get(t, l);
        }

        private Arpeggio(Signal[] s) {
            this.lengthInMs = s[0];
            for (int i=1; i<s.length; i++) {
                signals.add(s[i]);
                this.enveloped = this.enveloped || s[i].isEnveloped();
            }
        }
        
        public boolean isEnveloped() {
            return enveloped;
        }

    }

    public class ConstantArpeggio implements Signal {

        private long startTick = -1;
        private int current = 0;
        private double lengthInTicks;
        private Vector<Signal> signals = new Vector<Signal>();
        private boolean enveloped;
        
        public double get(long t, long l) {
            if (startTick == -1) {
                startTick = t;
            }
            if (t > startTick+lengthInTicks) {
                startTick = t;
                current++;
                if (current == signals.size()) {
                    current = 0;
                }
            }
            return signals.get(current).get(t, l);
        }

        private ConstantArpeggio(Signal[] s) {
            this.lengthInTicks = s[0].get(0, 0) * Apsynth.samplefreq / 1000;
            for (int i=1; i<s.length; i++) {
                signals.add(s[i]);
                this.enveloped = this.enveloped || s[i].isEnveloped();
            }
        }
        
        public boolean isEnveloped() {
            return enveloped;
        }

    }
}
