package de.cgarbs.apsynth;

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
    
    public double get(long step) {
    	if (step < data.length) {
    		return data[(int)step];
    	} else {
    		return 0;
    	}
    }

	public int getSamplerate() {
		return samplerate;
	}
	
}
