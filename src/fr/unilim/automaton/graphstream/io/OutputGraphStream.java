package fr.unilim.automaton.graphstream.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkGraphML;

/**
 * Class allowing to export an automaton to a file
 * @author romain
 *
 */
public class OutputGraphStream {
	
	private OutputGraphStream(){}
	
	/**
	 * Export an automaton to graphML file.
	 * 
	 * @param filePath
	 * @param toExport
	 * @throws IOException 
	 */
	public static void exportGraphToGraphML(String filePath, Graph toExport) throws IOException{
		FileSinkGraphML sink = new FileSinkGraphML();
		exportGraph(filePath, toExport, sink);
	}
	
	/**
	 * Export an automaton to a file
	 * @param filePath
	 * @param graph
	 * @param fileSink
	 * @throws IOException
	 */
	public static void exportGraph(String filePath, Graph graph, FileSink fileSink) throws IOException {
		OutputStream out;
		out = new FileOutputStream(filePath);
		fileSink.writeAll(graph, out);
	}
}
