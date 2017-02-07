package fr.unilim;

import java.io.BufferedReader;
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

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkGraphML;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

import scala.Char;
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
	}

}
