package fr.unilim.automaton.utils;

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
public class InputAutomaton {
	
	private InputAutomaton(){}
	
	/**
	 * Import an automaton from a file
	 * 
	 * @param filePath
	 * @return
	 */
	static Graph importGraph(String filePath) throws IOException {
		Graph outputGraph= new DefaultGraph("g");
		FileSource fs = FileSourceFactory.sourceFor(filePath);

		fs.addSink(outputGraph);	
		return outputGraph;
	}
}
