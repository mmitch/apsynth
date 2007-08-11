package de.cgarbs.apsynth.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * File interface for WAVE (and propably other RIFF) files.
 *
 * Author:  Christian Garbs <mitch@cgarbs.de>
 * License: GNU GPL
 */
class WaveFile extends RandomAccessFile {

    public WaveFile(String filename) throws FileNotFoundException, IOException {
        super(filename, "rw");
    }

    public void clearFile() throws IOException {
    	setLength(0);
    }

    public void writeString(String s) throws IOException {
        write(s.getBytes());
    }
    
    public void writeWord(short s) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(s);
        write(buffer.array());
    }

    public void writeDoubleWord(int i) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(i);
        write(buffer.array());
    }
    
    public String readString(int length) throws IOException {
        String string = "";
        for (int i=0; i<length; i++) {
            string += (char)this.read();
        }
        return string;
    }
    
    public short readWord() throws IOException {
        byte[] bytes= new byte[2];
        read(bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getShort();
    }
    
    public int readDoubleWord() throws IOException {
        byte[] bytes= new byte[4];
        read(bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }
    
}