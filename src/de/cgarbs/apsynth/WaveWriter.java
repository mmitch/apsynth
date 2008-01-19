package de.cgarbs.apsynth;
import java.io.IOException;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;


// TODO: make WaveWriter a true Signal
public class WaveWriter {

    Signal signal;
    de.cgarbs.apsynth.storage.WaveWriter out;

    public WaveWriter(Signal signal, String filename) {
        this.signal = signal;
        try {
        	out = new de.cgarbs.apsynth.storage.WaveWriter(filename, (short)2, (short)16, Apsynth.samplefreq);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            System.exit(1);
        }
    }

    public Stereo write(long tick) {
        if (out == null) {
            return new Stereo();
        }
        
        Stereo value = signal.get(tick, 0); 
        try {
	        out.write(value.l, value.r);
	    } catch (IOException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	        System.exit(1);
	    }
        return value;
    }

    public void close() {
        if (out != null) {
        	try {
	            out.close();
	        } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	            System.exit(1);
	        }
            out=null;
        }
    }
    
}
