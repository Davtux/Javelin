package fr.unilim.tests;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.tests.exception.APDUValuesReaderException;

/**
 * Class read concolic values files, 
 * generate during jdart execution
 * on Applet with concolic.values_file. 
 *
 */
public class APDUValuesReader implements Closeable, AutoCloseable {
	
	private final static Logger log = LoggerFactory.getLogger(APDUValuesReader.class);
	
	public final static String VALUE_TYPE = "byte";
	public final static String VALUE_NAME = "apdu.";

	private BufferedReader bufferReader;
	
	public APDUValuesReader(BufferedReader b) {
		this.bufferReader = b;
	}
	
	/**
	 * Read file and give one buffer.
	 * Return null if end of file.
	 * @return apdu buffer.
	 * @throws IOException 
	 * @throws APDUValuesReaderException 
	 */
	public byte[] nextBuffer() throws IOException, APDUValuesReaderException{
		String readline = "";
		String[] splitline;
		Pattern patternPosition = Pattern.compile("\\[(.*?)\\]");
		Matcher matcher;
		Map<Integer, Byte> postionsValues = new HashMap<>();
		
		Integer position;
		Byte value;
		
		while( (readline = this.bufferReader.readLine()) != null){
			splitline = readline.split(":");
			if(splitline.length != 3){
				throw new APDUValuesReaderException("Invalid line must be : type:name:value");
			}
			if(!VALUE_TYPE.equals(splitline[0])){
				log.warn("Ignore {} : type is not equal to {}", readline, VALUE_TYPE); 
				continue;
			}
			if(!splitline[1].contains(VALUE_NAME)){
				log.warn("Ignore {} : name is not equal to {}", readline, VALUE_NAME); 
				continue;
			}
			matcher = patternPosition.matcher(splitline[1]);
			if(!matcher.find()){
				throw new APDUValuesReaderException("Not found position in " + splitline[1]);
			}
			
			try {
				position = Integer.parseInt(matcher.group(1));
			} catch (Exception e) {
				throw new APDUValuesReaderException("Error dunring parse to integer " + matcher.group(1), e);
			}
			try{
				value = Byte.parseByte(splitline[2]);
			} catch (Exception e) {
				throw new APDUValuesReaderException("Error dunring parse to byte " + splitline[2], e);
			}
			
			if(postionsValues.containsKey(position)){
				this.bufferReader.reset();
				break;
			}
			postionsValues.put(position, value);
			this.bufferReader.mark(512);
		}
		
		if(postionsValues.isEmpty()){
			return null;
		}
		
		int sizeBuffer = Collections.max(postionsValues.keySet()) + 1;
		byte[] buffer = new byte[sizeBuffer];
		for(Map.Entry<Integer, Byte> entry : postionsValues.entrySet()){
			buffer[entry.getKey()] = entry.getValue();
		}
		
		return buffer;
	}

	@Override
	public void close() throws IOException {
		bufferReader.close();
	}
}
