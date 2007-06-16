package de.cgarbs.apsynth.instrument;

import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.note.Note;
import de.cgarbs.apsynth.note.NoteConverter;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass;


public abstract class Instrument {

    public abstract Note play(Signal freq, long length);
    
    public Note play(String note, long length) {
        return play(ConstantSignalClass.get(NoteConverter.getFrequency(note)), length);
    }
    
    public Note playArpeggio(long length, long phase, String[] notes) {
        Signal[] freqs = new Signal[notes.length+1];
        freqs[0] = ConstantSignalClass.get(phase);
        for (int i=0; i<notes.length; i++) {
            freqs[i+1] = ConstantSignalClass.get(NoteConverter.getFrequency(notes[i]));
        }
        Signal arp = Pool.getSignalClass("Arpeggio").instantiate(freqs);
        return play(arp, length);
    }
    
}
