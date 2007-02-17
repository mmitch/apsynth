package de.cgarbs.apsynth.storage;

import de.cgarbs.apsynth.Sample;
import de.cgarbs.apsynth.Track;
import de.cgarbs.apsynth.WaveWriter;

public interface StorageBackend {
    
    public void readInstruments(String filename);
    public void readSignals(String filename);
    public Track readTrack(String filename);
    public Sample readSample(String identifier);
	public WaveWriter readProject(String filename);
}
