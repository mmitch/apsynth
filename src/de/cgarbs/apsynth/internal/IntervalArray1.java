package de.cgarbs.apsynth.internal;

import java.util.SortedMap;
import java.util.TreeMap;

public class IntervalArray1 extends TreeMap implements IntervalArray {

    /**
     * 
     */
    private static final long serialVersionUID = 1477325302095841848L;

    long lastLow = 1;
    long lastHi = -1;
    double lastValue = 0;
    
    public double get(long key) {
        Long k = new Long(key);
        Double ret = (Double)super.get(k);
        
        if (ret == null) {
            SortedMap lowerValues = this.headMap(k);
            if (lowerValues != null) {
                ret = (Double)super.get(lowerValues.lastKey());
            } 
        }
        return ret.doubleValue();

        /*
         
         // TODO implement caching algorithm
         
        // query cache
    	if (key>=lastLow && key<lastHi) {
    		return lastValue;
    	}
    	// not cached, find value and boundaries for caching
        Long k = new Long(key);
        Double ret = (Double)super.get(k);
        
        if (ret == null) {
            SortedMap lowerValues = this.headMap(k);
            if (lowerValues != null) {
                ret = (Double)super.get(lowerValues.lastKey());
                lastLow = ((Long)lowerValues.lastKey()).longValue();
            } 
        } else {
        	lastLow = key;
        }
        Long = this.tailMap().lastKey(); // how to get successor()?
        lastValue = ret.doubleValue();
        return lastValue;
        */
    }

    public void put(long key, double value) {
        put(new Long(key), new Double(value));
    }
}
