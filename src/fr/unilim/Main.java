package fr.unilim;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkGraphML;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

public class Main {

	public static void main(String[] args) {
		Graph a = new SingleGraph("test");
		a.addAttribute("ui.antialias", true);
		
		Node n = a.addNode("etat_initial");
		n.addAttribute("ui.label", "Etat Initial");
		n.addAttribute("ui.style", "fill-mode: dyn-plain; fill-color: red, blue;");
		a.addNode("e1");
		a.addNode("e2");	
		a.addNode("etat_final");
		a.addNode("etat_erreur");
		
		a.addEdge("1", "etat_initial", "e1", true)
			.addAttribute("ui.label", "i < 10");
		a.addEdge("2", "etat_initial", "etat_erreur", true);
		a.addEdge("3", "e1", "e2", true);
		a.addEdge("4", "e1", "etat_erreur", true);
		a.addEdge("5", "e2", "etat_final", true);
		a.addEdge("6", "e2", "etat_erreur", true);
		
		//a.display();
		
		FileSinkGraphML sink = new FileSinkGraphML();
		OutputStream out;
		/*try {
			out = new FileOutputStream("test.xml");
			sink.writeAll(a, out);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		String filePath = "test.xml";
		Graph g = new DefaultGraph("g");
		FileSource fs = null;
		try {
			fs = FileSourceFactory.sourceFor(filePath);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		fs.addSink(g);

		try {
			fs.begin(filePath);

			while (fs.nextEvents()) {
				// Optionally some code here ...
			}
		} catch( IOException e) {
			e.printStackTrace();
		}
		

		try {
			fs.end();
		} catch( IOException e) {
			e.printStackTrace();
		} finally {
			fs.removeSink(g);
		}

		g.display();
		
	}

}
