package de.cgarbs.apsynth;

import de.cgarbs.apsynth.signal.Stereo;

public class Sample {

    private String name;
    private double data[];
    private int samplerate;

    public String getName() {
        return name;
    }
    
    public Sample(String name, int samplerate, double[] sampledata) {
        this.name = name;
        this.samplerate = samplerate;
        this.data = sampledata;
    }
    
    public Stereo get(long step) {
    	if (step < data.length) {
    		return new Stereo(data[(int)step]);
    	} else {
    		return new Stereo();
    	}
    }

	public int getSamplerate() {
		return samplerate;
	}
	
}
