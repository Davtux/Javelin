package fr.unilim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkGraphML;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

import fr.unilim.automaton.graphstream.xml.Graphml;
import scala.inline;

public class Main {

	public static void main(String[] args) throws IOException {
		/*FileReader in;
		Scanner sc = null;
		try {
			in = new FileReader("test.json");
			sc = new Scanner(in);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sc.nextLine());
		*/
		String charset = "UTF-8"; // or what corresponds
		  BufferedReader in = new BufferedReader( 
		      new InputStreamReader (new FileInputStream("test.json"), charset));
		  String line;
		  while( (line = in.readLine()) != null) {
			  line = line.replaceAll("\\\\u0026", "=");
			  line = line.replaceAll("\\\\u0027", "'");
			  line = line.replaceAll("\\\\u003c", "<");
			  line = line.replaceAll("\\\\u003e", ">");
			  line = line.replaceAll("\\\\u003d", ">");
			  System.out.println(line);
		  }
		  
		  
		  
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
	
	private static Graphml readFile(File file) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Graphml.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return (Graphml) unmarshaller.unmarshal(file);
	}

}
