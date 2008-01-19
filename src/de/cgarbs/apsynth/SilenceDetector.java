package de.cgarbs.apsynth;

import de.cgarbs.apsynth.signal.Stereo;

/**
 * monitors input signals and signalizes when there has been no input for a certain time
 * 
 * @author mitch
 *
 */
public class SilenceDetector {

    private long length;
    private long silence;
    private long lastTick;
    
    /**
     * default silence detection for 0.5s
     *
     */
    public SilenceDetector() {
        this(Apsynth.samplefreq/2);
    }
    
    /**
     * 
     * @param length silence time in ticks  
     */
    public SilenceDetector(long length) {
        this.length  = length;
        this.silence = 0;
        this.lastTick = 0;
    }
    
    /**
     * monitors a value
     * @param value signal value to monitor
     * @return the input value for convenience
     */
    public Stereo monitor(Stereo value, long tick) {
        if (Math.abs(value.l) + Math.abs(value.r) < 0.001) {
            silence += (tick-lastTick);
        } else {
            silence = 0;
        }
        lastTick = tick;
        return value;
    }
    
    /**
     * checks if the silence is long enough
     */
    public boolean isSilent() {
        return silence >= length;
    }
}
