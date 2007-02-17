package de.cgarbs.apsynth.signal.dynamic;

import de.cgarbs.apsynth.Rule;
import de.cgarbs.apsynth.signal.SignalClass;

public class SignalRule implements Rule {

	private SignalClass operator;
	private short operand[] = {};

	public SignalRule(SignalClass operator, short[] operand) {
		super();
		this.operator = operator;
		this.operand = operand;
	}
	public SignalClass getOperator() {
		return operator;
	}
	public void setOperator(SignalClass operator) {
		this.operator = operator;
	}
	
	public void setOperand(short idx, short value) {
		this.operand[idx] = value;
	}
	public short getOperand(short idx) {
		return this.operand[idx];
	}
	
	public String toString() {
		String s = "SignalRule["+operator.getName();
		for (int i=0; i<operator.getParamCount(); i++) {
			s+=", "+operand[i];
		}
		s+="]";
		return s;
	}
	
}
