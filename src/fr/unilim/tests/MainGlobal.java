package fr.unilim.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;
import org.json.simple.parser.ParseException;

import fr.unilim.Master;
import fr.unilim.automaton.algorithms.AutomatonCreator;
import fr.unilim.automaton.algorithms.exception.AlgorithmStateException;
import fr.unilim.automaton.graphstream.apdapter.AutomatonGraphml;
import fr.unilim.tree.IBinaryTree;
import fr.unilim.tree.adapter.BinaryTreeJSON;

public class MainGlobal {
	public static void main(String[] args) {
		
		Master master = new Master(Paths.get("/home/romain/Seafile/Travail/testGen"),Paths.get("/home/romain/Seafile/Travail/TPs_JAVACARD/Card_1/src"), "MyFutureApplet", "fr.unilim.tp1");
		master.execute(Paths.get("/home/romain/Seafile/Travail/jdart/jpf-core/bin/jpf"), Paths.get("/home/romain/Seafile/Travail/jdart/z3/build"));
		System.out.println("Generated successfully");
		
		System.setProperty("org.graphstream.ui.rendere", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		
		AutomatonCreator ac = new AutomatonCreator();
		
		FileInputStream f = null;
		try {
			f = new FileInputStream("/home/romain/Seafile/Travail/TPs_JAVACARD/Card_1/config.jpf.json");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			
			IBinaryTree tree = new BinaryTreeJSON(f);
			AutomatonGraphml a = (AutomatonGraphml) ac.parse(tree, new AutomatonGraphml("automaton"));
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
