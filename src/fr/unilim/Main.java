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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Char;
import fr.unilim.automaton.algorithms.AutomatonCreator;
import fr.unilim.automaton.graphstream.apdapter.AutomatonGraphml;
import fr.unilim.automaton.graphstream.xml.Graphml;
import fr.unilim.automaton.models.IAutomaton;
import scala.inline;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

import fr.unilim.automaton.graphstream.xml.Graphml;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws IOException {
		log.debug("Start Main ...");
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
		/*String charset = "UTF-8"; // or what corresponds
		  BufferedReader in = new BufferedReader( 
		      new InputStreamReader (new FileInputStream("test.json"), charset));
		  String line, group;
		  Pattern pattern = Pattern.compile("\\\\u....");
		  Pattern pattern2 = Pattern.compile("[0-9a-fA-F]{4}");
		  Matcher matcher, matcher2;
		  Integer i;
		  int toto;
		  while( (line = in.readLine()) != null) {
			  matcher = pattern.matcher(line);
			  while(matcher.find()){  
				  group = matcher.group(0);
				  matcher2 = pattern2.matcher(group);
				  while(matcher2.find()){
					  line = line.replaceAll("\\\\u"+matcher2.group(0),String.valueOf((char)Integer.parseInt(matcher2.group(0),16)));
				  }  
			  }
			  
			 
			  
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

			g.display();*/
		
		AutomatonCreator ac = new AutomatonCreator();
		FileInputStream f = new FileInputStream("test.json");
		try {
			AutomatonGraphml a = (AutomatonGraphml) ac.parse(f, new AutomatonGraphml("automaton"));
			a.getGraph().display();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		 
	}

}
