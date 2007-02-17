package de.cgarbs.apsynth.signal.dynamic;

import de.cgarbs.apsynth.Rule;

public class ConstantRule implements Rule {

	private double value;
	
	public ConstantRule(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public String toString() {
		return "ConstantRule["+value+"]";
	}
	
}
