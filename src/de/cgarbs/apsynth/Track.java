package de.cgarbs.apsynth;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.signal.Signal;

public class Track implements Signal {
    
    Vector activeNotes = new Vector();
    Map queue = new HashMap();
    
    public Track() {
        Pool.registerTrack(this);
    }
    
    public void queueNote(Note note, long time) {
        Long key = new Long(time);
        if (queue.containsKey(key)) {
            // add to existing timecode
            ((Vector)queue.get(key)).add(note);
        } else {
            // create new timecode and add
            Vector v = new Vector();
            v.add(note);
            queue.put(key, v);
        }
    }
    
    public double get(long tick) {
        double signal = 0;
        
        // check for new notes to be played
        Long key = new Long(tick);
        if (queue.containsKey(key)) {
            Vector newNotes = (Vector) queue.remove(key);
            Enumeration e = newNotes.elements();
            for (; e.hasMoreElements(); ) {
                activeNotes.add( (Note) e.nextElement());
            }
        }

        // play and remove existing notes
        Enumeration e = activeNotes.elements();
        for (; e.hasMoreElements(); ) {
            Note note = (Note)e.nextElement(); 
            signal += note.get(tick);
            if (note.isFinished(tick)) {
                activeNotes.remove(note);
            }
        }
        
        return signal;
    }
    
    public boolean isFinished() {
        return (activeNotes.isEmpty() && queue.isEmpty());
    }

    public void setParameters(Signal[] s) {
        // no-op: no parameters
    }

}
