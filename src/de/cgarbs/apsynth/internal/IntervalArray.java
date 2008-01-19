package de.cgarbs.apsynth.internal;

import de.cgarbs.apsynth.signal.Stereo;

public interface IntervalArray {

    public void put(long key, Stereo value);
    public Stereo get(long key);
}