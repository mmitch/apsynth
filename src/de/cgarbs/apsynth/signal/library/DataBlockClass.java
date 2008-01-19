package de.cgarbs.apsynth.signal.library;

import java.util.Vector;

import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.Stereo;

public class DataBlockClass extends DefaultSignalClass {

	public DataBlockClass() {
		this.paramCount = 0;
	}
	
    /**
     */    
	public Signal instantiate(Signal[] s) {
		checkParams(s);
		return new DataBlock();
	}
	
	public String getName() {
		return "DataBlock";
	}

    public static class DataBlock implements Signal {

        private int length;
        private Vector<Stereo> data;
        
        public DataBlock() {
            this.length = 0;
            this.data = new Vector<Stereo>();
        }
        
        public DataBlock(Vector<Stereo> data) {
        	this();
        	this.add(data);
        	this.length = this.data.size();
        }
        
        public DataBlock(double[] data) {
        	this();
        	this.add(data);
        	this.length = this.data.size();
        }
        
        public void add(double data) {
        	this.data.add(new Stereo(data));
        }
        
        public void add(Stereo data) {
        	this.data.add(data);
        	this.length++;
        }
        
        public void add(Vector<Stereo> data) {
        	this.data.addAll(data);
        	this.length = this.data.size();
        }
        
        public void add(double[] data) {
        	Vector<Stereo> tmp = new Vector<Stereo>();
        	for (int i=0; i<data.length; i++) {
        		tmp.add(new Stereo(data[i]));
        	}
        	this.add(tmp);
        }

        public void add(Stereo[] data) {
        	Vector<Stereo> tmp = new Vector<Stereo>();
        	for (int i=0; i<data.length; i++) {
        		tmp.add(new Stereo(data[i]));
        	}
        	this.add(tmp);
        }

        public Stereo get(long tick, long local) {
        	if (tick<length) {
                return data.get((int)local);
        	}
        	return new Stereo();
        }

        public int getLength() {
        	return data.size();
        }
        
        public boolean isEnveloped() {
            return false;
        }

    }
}
