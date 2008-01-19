package de.cgarbs.apsynth.signal.dynamic;
import java.util.Vector;

import de.cgarbs.apsynth.Rule;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.SignalClass;
import de.cgarbs.apsynth.signal.Stereo;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass;


public class DynamicSignalClass extends SignalClass {

	private Rule[] rules = {};
	private String name = "unnamed";
	
	public DynamicSignalClass(String name, int paramCount, Rule[] rules) {
		this.name = name;
		this.paramCount = paramCount;
		this.rules = rules;
	}
	
	public Signal instantiate(Signal[] s) {
		checkParams(s);
		return new DynamicSignal(rules, s);
	}
	
	public String getName() {
		return name;
	}
    
    public static class DynamicSignal implements Signal {

        private Signal lastVar;
        private boolean enveloped = false;

        private DynamicSignal(Rule[] r, Signal[] vars) {
            Vector<Signal> tmpVars = new Vector<Signal>();
            for (int i=0; i<vars.length; i++) {
                tmpVars.add(vars[i]);
            }
            
            for (int i=0; i<r.length; i++) {
                if (r[i] instanceof SignalRule) {
                    SignalRule rule = (SignalRule) r[i];
                    SignalClass sc = rule.getOperator();
                    Signal[] params = new Signal[sc.getParamCount()];
                    for (short param=0; param<sc.getParamCount(); param++) {
                        params[param] = tmpVars.get(rule.getOperand(param));
                    }
                    Signal newSignal = sc.instantiate(params);
                    if (newSignal.isEnveloped()) {
                        enveloped = true;
                    }
                    tmpVars.add(newSignal);

                } else if (r[i] instanceof ConstantRule) {
                    ConstantRule rule = (ConstantRule) r[i];
                    tmpVars.add(ConstantSignalClass.get(rule.getValue()));
                    
                } else {
                    // TODO throw proper exception
                    throw new RuntimeException("Unknown rule subclass encountered");
                }
            }
            lastVar = tmpVars.get(tmpVars.size()-1);
        }

        public Stereo get(long t, long l) {
            return lastVar.get(t, l);
        }

        public boolean isEnveloped() {
            return enveloped;
        }

    }
}
