package fr.unilim.automaton.graphstream.io;

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
public class InputGraphStream {
	
	private InputGraphStream(){}
	
	/**
	 * Import an automaton from a file
	 * 
	 * @param filePath
	 * @return
	 */
	public static Graph importGraph(String filePath) throws IOException {
		Graph outputGraph= new DefaultGraph("g");
		FileSource fs = FileSourceFactory.sourceFor(filePath);

		fs.addSink(outputGraph);	
		return outputGraph;
	}
}
