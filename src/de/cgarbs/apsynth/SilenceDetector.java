package de.cgarbs.apsynth;

/**
 * monitors input signals and signalizes when there has been no input for a certain time
 * 
 * @author mitch
 *
 */
public class SilenceDetector {

    private long length;
    private long silence;
    
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
    }
    
    /**
     * monitors a value
     * @param value signal value to monitor
     */
    public void monitor(double value) {
        if (value < 0.001) {
            silence++;
        } else {
            silence = 0;
        }
    }
    
    /**
     * checks if the silence is long enough
     */
    public boolean isSilent() {
        return silence >= length;
    }
}