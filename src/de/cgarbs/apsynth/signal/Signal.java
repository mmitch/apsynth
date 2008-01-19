package de.cgarbs.apsynth.signal;

public interface Signal {

	/**
     * calculate and return current signal
     * 
     * @param t global tick
     * @param l local tick
     * @return current signal
	 */
    public Stereo get(long t, long l);
	
    /**
     * tell if this signal is continously or time-dependend (enveloped)
     */
    public boolean isEnveloped();
}
