package de.cgarbs.apsynth.signal;

import java.util.HashMap;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.DataBlockClass.DataBlock;
import de.cgarbs.apsynth.signal.library.FiniteImpulseResponseClass.FiniteImpulseResponse;

abstract public class PassFilter implements Signal {

    protected FiniteImpulseResponse filter;
    protected int maxTaps = 0;
    protected Signal s_cutoff;
    protected Signal s_trband;
    protected Signal s_atten;
    protected Signal s_ripple;
    
    static protected HashMap<String,DataBlock> cache = new HashMap<String,DataBlock>();
    private String lastKey;
    protected String newKey;

    private boolean enveloped;

    public Stereo get(long t, long l) {
    	newKey = getHashKey(t, l);
    	if (! newKey.equals(lastKey) ) {
            if (cache.containsKey(newKey)) {
                filter.updateTaps(cache.get(newKey));
            } else {
                updateFilter(t, l);
            }
            lastKey = newKey;
            System.out.println("recalibrate!!! taps = "+maxTaps + ":" + newKey);
    	}
        return filter.get(t, l);
    }

    /**
     * 0: input signal
     * 1: cutoff frequency [Hz]
     * 2: transition bandwidth [Hz]
     * 3: stopband attenuation [dB]
     * 4: passband ripple [dB]
     */
    public PassFilter(Signal signal, Signal s_cutoff, Signal s_trband, Signal s_atten, Signal s_ripple) {

        // TODO: make filters changeable after initialization (lookout: needs cache)
    	this.s_cutoff = s_cutoff;
    	this.s_trband = s_trband;
    	this.s_atten  = s_atten;
    	this.s_ripple = s_ripple;
    	
    	lastKey = "";

        this.filter = new FiniteImpulseResponse(signal, new DataBlock(new double[]{1}));

        enveloped = signal.isEnveloped();
    }
    
    private String getHashKey(long t, long l) {
    	StringBuffer b = new StringBuffer();
    	b.append(maxTaps).append(':');
    	b.append(s_cutoff.get(t, l).getMono()).append(':');
    	b.append(s_trband.get(t, l).getMono()).append(':');
    	b.append(s_atten.get( t, l).getMono()).append(':');
    	b.append(s_ripple.get(t, l).getMono()).append(':');
    	return b.toString();
    }
    
    abstract protected void updateFilter(long t, long l);
            
    public boolean isEnveloped() {
        return enveloped;
    }

}

