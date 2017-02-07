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
	/**
	 * Import an automaton from a file
	 * 
	 * @param filePath
	 * @return
	 */
	static Graph importGraph(String filePath){
		Graph outputGraph= new DefaultGraph("g");
		FileSource fs = null;
		try {
			fs = FileSourceFactory.sourceFor(filePath);
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}

		fs.addSink(outputGraph);	
		return outputGraph;
	}
}
