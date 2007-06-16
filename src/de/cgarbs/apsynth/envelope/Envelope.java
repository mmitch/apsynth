package de.cgarbs.apsynth.envelope;

public interface Envelope {
    
    double get(long tick, long localTick, long duration);
    boolean isFinished(long tick, long localTick, long duration);

}
