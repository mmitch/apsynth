package de.cgarbs.apsynth.unittest;

import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.FiniteImpulseResponseClass;
import de.cgarbs.apsynth.signal.library.DataBlockClass.DataBlock;

public class FIRTest {

    public static void main(String argv[]) {
        
        Pool.registerSignalClass(new FiniteImpulseResponseClass());
    	
        DataBlock input = new DataBlock(new double[]{0, 1, 1, 1, 0, 0});
        DataBlock taps  = new DataBlock(new double[]{0.2, 0.3, 0.4});
        
        Signal fir = Pool.getSignalClass("FiniteImpulseResponse").instantiate(new Signal[]{input, taps}); 

        int length = input.getLength() + taps.getLength();
        
        for (long tick = 0;tick<length; tick++) {
        	System.out.println(tick+":"+fir.get(tick, tick));
        }
        
    }
    
}
