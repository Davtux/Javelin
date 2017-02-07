package fr.unilim.automaton.utils;

import java.io.BufferedReader;
import java.io.IOException;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

/**
 * Class allowing to import an automaton from a file
 * @author romain
 *
 */
public class JDartOutputParser {
	
	private String JDartOutputParser(BufferedReader toParse) throws IOException{
		String line, toReturn;
		toReturn = new String();
		while( (line = toParse.readLine()) != null) {
			  line = line.replaceAll("\\\\u0026", "=");
			  line = line.replaceAll("\\\\u0027", "'");
			  line = line.replaceAll("\\\\u003c", "<");
			  line = line.replaceAll("\\\\u003e", ">");
			  line = line.replaceAll("\\\\u003d", ">");
			  
			  toReturn += line;
		  }
		return toReturn;
	}
	
}
