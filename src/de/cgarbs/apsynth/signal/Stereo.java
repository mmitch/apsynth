package de.cgarbs.apsynth.signal;

public class Stereo {
	
	public double l;
	public double r;
	
	public Stereo() {
		this.l = 0;
		this.r = 0;
	}
	
	public Stereo(double left, double right) {
		this.l = left;
		this.r = right;
	}
	
	public Stereo(double both) {
		this(both, both);
	}

	public Stereo(Stereo other) {
		this(other.l, other.r);
	}

	public Stereo mix(Stereo other) {
		this.l += other.l;
		this.r += other.r;
		return this;
	}
	
	public double getMono() {
		return (r+l)/2;
	}
	
	public boolean equals(Stereo other) {
		return (this.l == other.l) && (this.r == other.r);
	}
	
}
