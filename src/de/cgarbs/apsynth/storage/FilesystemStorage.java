package de.cgarbs.apsynth.storage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import de.cgarbs.apsynth.Apsynth;
import de.cgarbs.apsynth.Note;
import de.cgarbs.apsynth.Rule;
import de.cgarbs.apsynth.Sample;
import de.cgarbs.apsynth.Track;
import de.cgarbs.apsynth.WaveWriter;
import de.cgarbs.apsynth.instrument.Instrument;
import de.cgarbs.apsynth.instrument.InstrumentClass;
import de.cgarbs.apsynth.internal.Pool;
import de.cgarbs.apsynth.signal.Signal;
import de.cgarbs.apsynth.signal.SignalClass;
import de.cgarbs.apsynth.signal.dynamic.ConstantRule;
import de.cgarbs.apsynth.signal.dynamic.DynamicSignalClass;
import de.cgarbs.apsynth.signal.dynamic.SignalRule;
import de.cgarbs.apsynth.signal.library.ConstantSignalClass;
import de.cgarbs.apsynth.signal.library.RegisterClass;

public class FilesystemStorage implements StorageBackend {

    public void readInstruments(String filename) {
        throw new RuntimeException(this.getClass().getName() + ".readInstrument(): ENOIMPLEMENT");
    }

    public void readSignals(String filename) {
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            
            HashMap<String,Short> signalRulesIndex = new HashMap<String,Short>();
            Vector<Rule> signalRules = new Vector<Rule>();
            String line;
            String signalClassName = "";
            short signalVarCount = 0;
            int lineCount = 0;
            
            while ((line = in.readLine()) != null) {
            	lineCount++;
                String[] token = line.split("\\s+");

                try {
                
		            // empty line
		            if (line.length() == 0 || token.length == 0) {
		
		            // comment line
		            } else if (token[0].equals("*")) {

		            // a new Signal to be defined
		            } else if (token[0].equals("define")) {
		            	if (token.length == 3) {
		            		signalClassName = token[1];
		            		try {
		            			signalVarCount = Short.parseShort(token[2]);
		            		} catch (NumberFormatException e) {
		            			throw new ParseException("variable count in signal definition is not numeric", e);
		            		}
		            		signalRulesIndex.clear();
		            		signalRules.clear();
		            		for (short i = 0; i<signalVarCount; i++) {
		            			signalRulesIndex.put("var"+i, new Short(i));
		            			signalRules.add(null);
		            		}
		            	} else {
            				throw new ParseException("Signal definition line with != 2 arguments");
		            	}
		            	
		            // definition line
		            } else {
		            	if (token.length > 1) {
		            		String ruleName = token[0];
		            		String signalName = token[1];
		            		SignalRule signalRule = null;

		            		SignalClass filterClass = Pool.getSignalClass(signalName);
	            			int paramCount = filterClass.getParamCount();
	            			if (paramCount != token.length-2) {
	            				throw new ParseException("wrong argument count for SignalClass");
	            			}
	            			
	            			short[] parameters = new short[paramCount];
	            			for (int i=0; i<paramCount; i++) {
	            				String param = token[i+2];
	            				Double d = null;
	            				try {
	            					d = new Double(param);
	            				} catch (NumberFormatException n) {
	            					// it's no value, it's another signal
	            				}
	            				if (d == null) {
	            					checkSignal(param, signalRulesIndex);
	            					parameters[i] = signalRulesIndex.get(param).shortValue();
	            				} else {
	            					parameters[i] = (short)signalRules.size();
	            					signalRules.add(new ConstantRule(d.doubleValue()));
	            				}
	            			}
	            			signalRule = new SignalRule(filterClass, parameters);
	            			signalRulesIndex.put(ruleName, new Short((short)signalRules.size()));
		            		signalRules.add(signalRule);
		            	} else {
            				throw new ParseException("too few arguments");
		            	}
		            }
		            
		            // return: save defined SignalClass in Pool
	            	if (token[0].equals("return")) {
	            		int ruleCount = signalRules.size() - signalVarCount;
	            		Rule[] rules = new Rule[ruleCount];
	            		for (int i=0; i<ruleCount; i++) {
	            			rules[i] = signalRules.get(i + signalVarCount);
	            		}
	            		Pool.registerSignalClass(
	            				new DynamicSignalClass(signalClassName, signalVarCount, rules)
	            				);
	            	}
		            
            	} catch (ParseException p) {
            		throw new RuntimeException(p.getMessage()+" [in project "+filename+", line "+lineCount+"]", p);
            	}
            		
            }
            
            in.close();
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Track readTrack(String filename) {
        Track track = new Track();
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            
            Instrument instrument = Pool.getInstrumentClass("Null").instanciate(new Signal[]{});
            double step = Apsynth.samplefreq * 60 / 120;
            double position = 0;
            
            HashMap<String,Signal> signals = new HashMap<String,Signal>();
            String line;
            int lineNumber = 0;
            
            while ((line = in.readLine()) != null) {
            	lineNumber++;
                String[] commands = line.split("\\s*;\\s*");
                
                try {
	                for (int c = 0; c < commands.length; c++) {
	                    String[] token = commands[c].split("\\s+");
	                    
	                    // empty line
	                    if (line.length() == 0 || token.length == 0) {
	
	                    // comment line
	                    } else if (token[0].equals("*")) {
	                        c = commands.length;  // skip remaining line
	
	                    // change instrument
	                    } else if (token[0].equals("instrument")) {
	                    	if (token.length < 2) {
	                    		throw new ParseException("too few arguments in instrument definition");
	                    	}
	                    	InstrumentClass instrumentClass = Pool.getInstrumentClass(token[1]);
	                    	int paramCount = instrumentClass.getParamCount();
	                    	if (token.length - 2 != paramCount) {
	                    		throw new ParseException("wrong argument count in instrument definition (expected "+paramCount+", got "+(token.length-2)+")");
	                    	}
	                    	Signal[] instrumentSignals = new Signal[paramCount];
	                    	
	                    	for (int i=0; i<paramCount; i++) {
	                			instrumentSignals[i] = parseSignalParameter(signals, token[i+2]);
	                    	}
	                    	
	                        instrument = instrumentClass.instanciate(instrumentSignals);
	                        // TODO error checking
	
	                    // define a signal
	                    } else if (token[0].equals("signal")) {
	                    	if (token.length < 3) {
	                    		throw new ParseException("too few arguments in signal definition");
	                    	}
	                    	String signalName = token[1];
	                    	Signal signal = parseSignalLine(signals, token, 3, token[2]);
	                    	signals.put(signalName, signal);
	                    	
	                    // change speed
	                    } else if (token[0].equals("speed")) {
                            double d = Double.parseDouble(token[1]);
                            if (d > 0) {
                                step = Apsynth.samplefreq * 60 / d;
                            } else {
                                step = 0;
                            }
	                        // TODO error checking
	
                        // set register value
                        } else if (token[0].equals("arp")) {
                            long length = (long)(Double.parseDouble(token[1]) * step);
                            long phase  = (long)Double.parseDouble(token[2]);
                            String[] notes = new String[token.length-3];
                            for (int i = 3; i<token.length; i++) {
                                notes[i-3] = token[i];
                            }
                            Note note = instrument.playArpeggio(length, phase, notes);
                            track.queueNote(note, (long)position);
                            
                        // set register value
                        } else if (RegisterClass.isRegister(token[0])) {
                            double value = Double.parseDouble(token[1]);
                            Pool.getRegister(token[0]).set((long)position, value);
                            
                            
	                    // play note
	                    } else {
	                        Note note = instrument.play(token[0], (long)(Double.parseDouble(token[1]) * step));
	                        track.queueNote(note, (long)position);
	                        
	                    }
	                    
	                }
            	} catch (ParseException p) {
            		throw new RuntimeException(p.getMessage()+" [in project "+filename+", line "+lineNumber+"]", p);
            	}
                position += step;
            }
            
            in.close();
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return track;
    }

    public Sample readSample(String identifier) {

    	Sample sample = null;
    	
    	try {
    		WaveFile in = new WaveFile(identifier+".wav");
    	
    		// WAV file structure see  http://www.lightlink.com/tjweber/StripWav/Canon.html
    		
    		// RIFF HEADER
    		//  Offset  Length   Contents
    		//  0       4 bytes  'RIFF'
    		//  4       4 bytes  <file length - 8>
    		//  8       4 bytes  'WAVE'
    		
    		if (!in.readString(4).equals("RIFF")) {
    			throw new RuntimeException("file is no RIFF file");
    		}
    		in.readDoubleWord();
    		if (!in.readString(4).equals("WAVE")) {
    			throw new RuntimeException("file is no WAVE format");
    		}
    		// FMT CHUNK
    		//  12      4 bytes  'fmt '
    		//  16      4 bytes  0x00000010     // Length of the fmt data (16 bytes)
    		//  20      2 bytes  0x0001         // Format tag: 1 = PCM
    		//  22      2 bytes  <channels>     // Channels: 1 = mono, 2 = stereo
    		//  24      4 bytes  <sample rate>  // Samples per second: e.g., 44100
    		//  28      4 bytes  <bytes/second> // sample rate * block align
    		//  32      2 bytes  <block align>  // channels * bits/sample / 8
    		//  34      2 bytes  <bits/sample>  // 8 or 16
    		if (!in.readString(4).equals("fmt ")) {
    			throw new RuntimeException("fmt chunk expected");
    		}
    		if (in.readDoubleWord() != 16) {
    			throw new RuntimeException("wrong length of format chunk");
    		}
    		if (in.readWord() != 1) {
    			throw new RuntimeException("unsupported format (!=PCM)");
    		}
    		int channels = in.readWord();
    		int samplerate = in.readDoubleWord();
    		in.readDoubleWord();
    		int blockalign = in.readWord();
    		int bits = in.readWord();

    		// DATA CHUNK
    		//  36      4 bytes  'data'
    		//  40      4 bytes  <length of the data block>
    		//  44        bytes  <sample data>
    		if (!in.readString(4).equals("data")) {
    			throw new RuntimeException("data chunk expected");
    		}
    		int datalength = in.readDoubleWord();

    		int samples = datalength/blockalign;
    		double sampledata[] = new double[samples];
    		if (channels == 1) {
    			if (bits == 8) {
    	    		for (int i=0; i<samples; i++) {
    	    			sampledata[i] = (((double)in.read())/128)-1;
    	    		}
        		} else {
    	    		for (int i=0; i<samples; i++) {
    	    			sampledata[i] = (double)in.readWord()/32768;
    	    		}
        		}
    	    } else {
    			if (bits == 8) {
    	    		for (int i=0; i<samples; i++) {
    	    			sampledata[i] = (((double)in.read()+(double)in.read())/256)-1;
    	    		}
        		} else {
    	    		for (int i=0; i<samples; i++) {
    	    			sampledata[i] = ((double)in.readWord()+(double)in.readWord())/65536;
    	    		}
        		}
    		}
    		
    		sample = new Sample(identifier, samplerate, sampledata);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    	
        return sample;
    }

	public WaveWriter readProject(String filename) {
        WaveWriter w = null;
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            
            HashMap<String,Signal> signals = new HashMap<String,Signal>();
            String line;
            int lineNumber = 0;
            
            while ((line = in.readLine()) != null) {
            	lineNumber++;
                String[] token = line.split("\\s+");

                try {
                
		            // empty line
		            if (line.length() == 0 || token.length == 0) {
		
		            // comment line
		            } else if (token[0].equals("*")) {
		                    
			        // instruments (parse instrument file)
		            } else if (token[0].equals("instruments")) {
		            	checkLength(token,2);
		            	readInstruments(token[1]);
			            
			        // signals (parse signal file)
		            } else if (token[0].equals("signals")) {
		            	checkLength(token,2);
		            	readSignals(token[1]);
			            
		            // return (finish)
		            } else if (token[0].equals("return")) {
		            	checkLength(token, 3);
		            	checkSignal(token[1], signals);
		        		w = new de.cgarbs.apsynth.WaveWriter((Signal)signals.get(token[1]), token[2]);
		            } else {
		            	if (token.length > 1) {
		            		String name = token[0];
		            		String filterName = token[1];
		            		Signal signal = null;
		            		if (filterName.equals("Track")) {
		            			checkLength(token, 3);
		            			signal = readTrack(token[2]);
		            		} else {
		            			signal = parseSignalLine(signals, token, 2, filterName);
		            		}
		            		signals.put(name, signal);
		            	} else {
            				throw new ParseException("too few arguments");
		            	}
		            }
            	} catch (ParseException p) {
            		throw new RuntimeException(p.getMessage()+" [in project "+filename+", line "+lineNumber+"]", p);
            	}
            		
            }
            
            in.close();
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return w;
	}

	/**
	 * generate a Signal from a line in a file
	 * @param signals HashMap containing all currently known signals
	 * @param token array of tokens to be parsed
	 * @param offset first token to be parsed that contains a parameter to the SignalClass
	 * @param name SignalClass name
	 * @return the new Signal
	 * @throws ParseException something went wrong
	 */
	private Signal parseSignalLine(HashMap<String,Signal> signals, String[] token, int offset, String name) throws ParseException {
		Signal signal;
		SignalClass filterClass = Pool.getSignalClass(name);
		int paramCount = filterClass.getParamCount();
		if (paramCount != token.length-offset) {
			throw new ParseException("wrong argument count for SignalClass");
		}
		Signal[] parameters = new Signal[paramCount];
		for (int i=0; i<paramCount; i++) {
			parameters[i] = parseSignalParameter(signals, token[i+offset]);
		}
		signal = filterClass.instanciate(parameters);
		return signal;
	}

	/**
	 * Transform a parameter string into a signal
	 * @param signals all known Signals
	 * @param param String to parse
	 * @throws ParseException something went wrong
	 */
	private Signal parseSignalParameter(HashMap<String,Signal> signals, String param) throws ParseException {
		Double d = null;
		try {
			d = new Double(param);
		} catch (NumberFormatException n) {
			// it's no value, it's another signal
		}
		if (d == null) {
            if (param.startsWith("R_")) {
                // it's a register!
                return Pool.getRegister(param);
            }
			checkSignal(param, signals);
			return signals.get(param);
		} else {
			return ConstantSignalClass.get(d.doubleValue());
		}
	}
	
	private void checkLength(String[] s, int l) throws ParseException {
		if (s.length != l) {
			throw new ParseException("wrong argument count (expected "+l+", got "+s.length+")");
		}
	}

	private void checkSignal(String name, HashMap all) throws ParseException {
		if (! all.containsKey(name)) {
			throw new ParseException("undefined Signal name "+name);
		}
	}
	
	private class ParseException extends Exception {
		
		ParseException(String message) {
			super(message);
		}

		ParseException(String message, Throwable t) {
			super(message, t);
		}

		private static final long serialVersionUID = 5731989930835038495L;
	}
    
}
