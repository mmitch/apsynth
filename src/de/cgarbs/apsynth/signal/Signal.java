package de.cgarbs.apsynth.signal;

public interface Signal {

	/**
     * calculate and return current signal
     * 
     * @param t global tick
     * @param l local tick
     * @return current signal
	 */
    public double get(long t, long l);
	
}
