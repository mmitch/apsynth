package de.cgarbs.apsynth.signal.library;

import java.util.Vector;

import de.cgarbs.apsynth.signal.Signal;

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
        private Vector<Double> data;
        
        public DataBlock() {
            this.length = 0;
            this.data = new Vector<Double>();
        }
        
        public DataBlock(Vector<Double> data) {
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
        	this.data.add(data);
        	this.length++;
        }
        
        public void add(Vector<Double> data) {
        	this.data.addAll(data);
        	this.length = this.data.size();
        }
        
        public void add(double[] data) {
        	Vector<Double> tmp = new Vector<Double>();
        	for (int i=0; i<data.length; i++) {
        		tmp.add(new Double(data[i]));
        	}
        	this.add(tmp);
        }

        public double get(long tick, long local) {
        	if (tick<length) {
                return data.get((int)local);
        	}
        	return 0;
        }

        public int getLength() {
        	return data.size();
        }
        
        public boolean isEnveloped() {
            return false;
        }

    }
}
