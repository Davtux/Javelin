package fr.unilim.automaton.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class allowing to import an automaton from a file
 * @author romain
 *
 */
public class JDartOutputParser {
	
	private JDartOutputParser(){}
	
	public static String parseJDartOutput(BufferedReader toParse) throws IOException{
		String line;
		String group;
		StringBuilder toReturn = new StringBuilder();
		Pattern pattern = Pattern.compile("\\\\u....");
		Pattern pattern2 = Pattern.compile("[0-9a-fA-F]{4}");
		Matcher matcher;
		Matcher matcher2;
		while( (line = toParse.readLine()) != null) {
			matcher = pattern.matcher(line);
			while(matcher.find()){  
				group = matcher.group(0);
				matcher2 = pattern2.matcher(group);
				while(matcher2.find())
					line = line.replaceAll("\\\\u"+matcher2.group(0),String.valueOf((char)Integer.parseInt(matcher2.group(0),16)));  
			}
			toReturn.append(line);
		}
		return toReturn.toString();
	}
	
}
