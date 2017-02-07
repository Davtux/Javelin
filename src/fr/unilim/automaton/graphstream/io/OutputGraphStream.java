package fr.unilim.automaton.graphstream.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSinkGraphML;

/**
 * Class allowing to export an automaton to a file
 * @author romain
 *
 */
public class OutputGraphStream {
	
	private OutputGraphStream(){}
	
	/**
	 * Export an automaton to a file
	 * 
	 * @param filePath
	 * @param toExport
	 * @throws IOException 
	 */
	static void exportGraph(String filePath, Graph toExport) throws IOException{
		FileSinkGraphML sink = new FileSinkGraphML();
		OutputStream out;
		out = new FileOutputStream(filePath);
		sink.writeAll(toExport, out);	
	}
}
