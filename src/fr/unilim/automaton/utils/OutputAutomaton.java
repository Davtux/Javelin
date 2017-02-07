package fr.unilim.automaton.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSinkGraphML;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

/**
 * Class allowing to export an automaton to a file
 * @author romain
 *
 */
public class OutputAutomaton {
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
		out = new FileOutputStream("test.xml");
		sink.writeAll(toExport, out);	
	}
}
