package de.cgarbs.apsynth;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.note.Note;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;

public class Track implements Signal {
    
    Vector<Note> activeNotes = new Vector<Note>();
    Map<Long,Vector<Note>> queue = new HashMap<Long,Vector<Note>>();
    
    long lastTick;
    
    public Track() {
        Pool.registerTrack(this);
        this.lastTick = 0;
    }
    
    public void queueNote(Note note, long time) {
        Long key = new Long(time);
        if (queue.containsKey(key)) {
            // add to existing timecode
            queue.get(key).add(note);
        } else {
            // create new timecode and add
            Vector<Note> v = new Vector<Note>();
            v.add(note);
            queue.put(key, v);
        }
    }
    
    public Stereo get(long tick, long local) {
        Stereo signal = new Stereo();

        for (long checkTick = lastTick; checkTick <= tick; checkTick++) {
            // check for new notes to be played
            Long key = new Long(checkTick);
            if (queue.containsKey(key)) {
                Vector newNotes = (Vector) queue.remove(key);
                Enumeration e = newNotes.elements();
                for (; e.hasMoreElements(); ) {
                    activeNotes.add( (Note) e.nextElement());
                }
            }
        }
        lastTick = tick;
    
        // play and remove existing notes
        Enumeration e = activeNotes.elements();
        for (; e.hasMoreElements(); ) {
            Note note = (Note)e.nextElement(); 
            signal.mix(note.get(tick, local));
            if (note.isFinished()) {
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

    public boolean isEnveloped() {
        return true;
    }

}
