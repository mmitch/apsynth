package de.cgarbs.apsynth.note;

import java.util.HashMap;
import java.util.Map;

public class NoteConverter {

    static Map<String,Integer> notes = null;
    static double freqs[] = new double[12];
    static Map<String,Double> cache = new HashMap<String,Double>();
    
    private static void initialize() {
        notes = new HashMap<String,Integer>();

        notes.put("c", new Integer(-9));
        notes.put("d", new Integer(-7));
        notes.put("e", new Integer(-5));
        notes.put("f", new Integer(-4));
        notes.put("g", new Integer(-2));
        notes.put("a", new Integer( 0));
        notes.put("h", new Integer( 2));
        notes.put("b", new Integer( 2));
        notes.put("C", new Integer(-9));
        notes.put("D", new Integer(-7));
        notes.put("E", new Integer(-5));
        notes.put("F", new Integer(-4));
        notes.put("G", new Integer(-2));
        notes.put("A", new Integer( 0));
        notes.put("H", new Integer( 2));
        notes.put("B", new Integer( 2));
        
        for (byte i=0; i<12; i++) {
            freqs[i] = 440 * Math.pow(2 ,i/12.0);
        }
    }
    
    public static double getFrequency(String noteString) {
        if (notes == null) {
            initialize();
        }
    
        if (cache.containsKey(noteString)) {
            return ((Double) cache.get(noteString)).doubleValue();
        }
        
        String note = noteString.substring(0, 1);
        String mod;
        byte oct;
        if (noteString.length() == 3) {
            mod = noteString.substring(1, 2);
            oct = Byte.parseByte(noteString.substring(2, 3));
        } else {
            mod = "";
            oct = Byte.parseByte(noteString.substring(1, 2));
        }
        
        byte index = 0;
        if (notes.containsKey(note)) {
            index = ((Integer)notes.get(note)).byteValue();
        }
        if (mod.equals("#")) {
            index ++;
        } else if (mod.equalsIgnoreCase("b")) {
            index --;
        }
        if (index < 0) {
            index += 12;
            oct --;
        } else if (index > 11){
            index -= 12;
            oct ++;
        }
        double freq = freqs[index] * Math.pow(2, oct-4);
        cache.put(noteString, new Double(freq));
        // System.out.println(noteString+" --> "+freq+"Hz");
        return freq;
    }
    
}
