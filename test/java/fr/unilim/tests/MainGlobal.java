package fr.unilim.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;
import org.json.simple.parser.ParseException;

import fr.unilim.Config;
import fr.unilim.automaton.algorithms.AutomatonCreator;
import fr.unilim.automaton.algorithms.exception.AlgorithmStateException;
import fr.unilim.automaton.graphstream.apdapter.AutomatonGraphStream;
import fr.unilim.concolic.Master;
import fr.unilim.concolic.exception.ConcolicExecutionException;
import fr.unilim.tree.IBinaryTree;
import fr.unilim.tree.adapter.BinaryTreeJSON;

public class MainGlobal {
	public static void main(String[] args) {
		try {
			Config.loadConfigFile("ProjetM1.conf");
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		Master master = new Master(
				Paths.get("test/resources/PorteMonnaie/src/"), 
				"PorteMonnaie", 
				"fr.unilim");
		try {
			master.execute(Paths.get(Config.getZ3BuildPath()));
		} catch (ConcolicExecutionException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("Generated successfully");
		
		System.setProperty("org.graphstream.ui.rendere", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		
		AutomatonCreator ac = new AutomatonCreator();
		
		FileInputStream f = null;
		try {
			f = new FileInputStream("SUT/config.jpf.json");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			
			IBinaryTree tree = new BinaryTreeJSON(f);
			AutomatonGraphStream a = (AutomatonGraphStream) ac.parse(tree, new AutomatonGraphStream("automaton"));
			Graph g = a.getGraph();
			g.setAttribute("layout.quality", 4);
			g.setAttribute("layout.weight", 0);
			Viewer v = g.display();
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
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
