package fr.unilim.tests;

import java.io.FileInputStream;
import java.io.IOException;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.automaton.algorithms.AutomatonCreator;
import fr.unilim.automaton.algorithms.exception.AlgorithmStateException;
import fr.unilim.automaton.graphstream.apdapter.AutomatonGrapStream;
import fr.unilim.filter.automaton.impl.ResultFilter;
import fr.unilim.filter.exception.FilterException;
import fr.unilim.tree.IBinaryTree;
import fr.unilim.tree.adapter.BinaryTreeJSON;

public class MainGraph {

	private static final Logger log = LoggerFactory.getLogger(MainGraph.class);
	
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
		System.setProperty("org.graphstream.ui.rendere", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		AutomatonCreator ac = new AutomatonCreator();
		FileInputStream f = new FileInputStream("test/resources/test_arrays_m4.json");
		try {
			IBinaryTree tree = new BinaryTreeJSON(f);
			AutomatonGrapStream a = (AutomatonGrapStream) ac.parse(tree, new AutomatonGrapStream("automaton"));
			
			Graph g = a.getGraph();
			g.setAttribute("layout.quality", 4);
			g.setAttribute("layout.weight", 0);
			Viewer v = g.display();
			v.enableAutoLayout();
			Thread.sleep(2000);
			v.disableAutoLayout();
			
			ResultFilter filter = new ResultFilter(a.getFinalStates().get(0).getName());
			AutomatonGrapStream a_filterd = new AutomatonGrapStream("filter");
			filter.doFilter(a, a_filterd);
			
			g = a_filterd.getGraph();
			g.setAttribute("layout.quality", 4);
			g.setAttribute("layout.weight", 0);
			v = g.display();
			v.enableAutoLayout();
			Thread.sleep(2000);
			v.disableAutoLayout();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (AlgorithmStateException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FilterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		f.close();
		 
	}

}
