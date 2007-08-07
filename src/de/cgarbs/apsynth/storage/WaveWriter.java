package de.cgarbs.apsynth.storage;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Renders an audio stream into a WAV file.
 *
 * Author:  Christian Garbs <mitch@cgarbs.de>
 * License: GNU GPL
 * 
 * WAV file format according to http://www.lightlink.com/tjweber/StripWav/Canon.html
 */
public class WaveWriter {

	private short channels;
	private short blockalign;
	private int samples;
	private short bitsize;
	WaveFile out;
	
    /**
     * Default constructor
     */
    public WaveWriter()
    {
    };

    /**
     * Convenience constructur, calls open() method.
     *
     * @param filename The name of the file to write to
     * @param channels The number of channels (1==mono, 2==stereo)
     * @param bitsize  The sample size (eg. 8 or 16)
     * @param rate     The sample rate (eg. 44100 or 22050)
     */
    public WaveWriter(String filename, short channels, short bitsize, int rate) throws FileNotFoundException, IOException
    {
        super();
        this.open(filename, channels, bitsize, rate);
    };

    /**
     * Opens a file for writing.
     *
     * @param filename The name of the file to write to
     * @param channels The number of channels (1==mono, 2==stereo)
     * @param bitsize  The sample size (eg. 8 or 16)
     * @param rate     The sample rate (eg. 44100 or 22050)
     */
    public void open(String filename, short channels, short bitsize, int rate) throws FileNotFoundException, IOException
    {
    	this.bitsize = bitsize;
    	this.channels = channels;
    	blockalign = (short)(channels * bitsize / 8);

    	out = new WaveFile(filename);
        System.out.println("writing to "+filename);
    	
    	// RIFF
    	out.writeString("RIFF");
    	out.writeDoubleWord(0); // length (overwritten)
    	out.writeString("WAVE");
    	
    	// FMT CHUNK
    	out.writeString("fmt ");
    	out.writeDoubleWord(16);
    	out.writeWord((short)1);
    	out.writeWord(channels);
    	out.writeDoubleWord(rate);
    	out.writeDoubleWord(rate*blockalign);
    	out.writeWord(blockalign);
    	out.writeWord(bitsize);
    	
    	// DATA CHUNK
    	out.writeString("data");
    	out.writeDoubleWord(0); // length (overwritten)
    };

    /**
     * Writes one mono sample to the file.
     * (On stereo file, upmix is done automatically.)
     *
     * @param sample The sample to write (valid range: -1 to +1)
     */
    public void write(double sample) throws IOException
    {
        if ((sample < -1) || (sample > 1)) {
            System.out.println("CLIP");
        }
        
		if (bitsize == 8) {
			byte val = (byte)((sample+1)*127); 
			out.write(val);
    		if (channels == 2) {
    			out.write(val);
    		}
		} else {
			short val = (short)(sample*32767);
			out.writeWord(val);
    		if (channels == 2) {
    			out.writeWord(val);
    		}
    	}
		samples++;
    };

    /**
     * Writes one stereo sample to the file.
     * (On mono file, downmix is done automatically.)
     *
     * @param sample_l The sample to write (left channel) (valid range: -1 to +1)
     * @param sample_r The sample to write (right channel) (valid range: -1 to +1)
     */
    public void write(double sample_l, double sample_r) throws IOException
    {
		if (bitsize == 8) {
    		if (channels == 1) {
    			byte val = (byte)((sample_l+sample_r+2)*64); 
				out.write(val);
				out.write(val);
    		} else {
    			out.write((byte)((sample_l+1)*127));
    			out.write((byte)((sample_r+1)*127));
    		}
		} else {
    		if (channels == 1) {
    			short val = (short)((sample_l+sample_r)*16383);
    			out.writeWord(val);
    			out.writeWord(val);
    		} else {
    			out.writeWord((short)(sample_l*32767));
    			out.writeWord((short)(sample_r*32767));
    		}
    	}
		samples++;
    };

    /**
     * Close wave file.
     */
    public void close() throws IOException
    {
        // update file length
        out.seek(4);
    	out.writeDoubleWord(samples * blockalign + 32); 
    	// update data block length
        out.seek(40);
    	out.writeDoubleWord(samples * blockalign); 
    	out.close();
    }
}
