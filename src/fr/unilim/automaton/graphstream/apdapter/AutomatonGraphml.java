package fr.unilim.automaton.graphstream.apdapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.automaton.models.IAutomaton;
import fr.unilim.automaton.models.StateNotFoundException;

public class AutomatonGraphml implements IAutomaton {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String ATTR_LABEL = "ui.label";
	
	private Graph graph;
	private Set<String> finalStates;
	private Node intialState;
	private boolean side;
	
	public AutomatonGraphml(String name){
		this.graph = new MultiGraph(name); // MutliGraph for prevent EdgeRejetedException
		this.enableHQ();
		this.side = true;
		this.finalStates = new HashSet<String>();
		this.intialState = this.graph.addNode("init");
		this.intialState.setAttribute("ui.class", "initial");
		this.graph.addAttribute("ui.stylesheet", "url('file:///home/romain/Seafile/Travail/ProjetM1/graph.css')");
	}

	private void enableHQ() {
		this.graph.addAttribute("ui.quality", true);
		this.graph.addAttribute("ui.antialias", true);
	}

	public boolean addFinalState(String name, String label) {
		Node n = null;
		try {
			n = graph.addNode(name);
		} catch(IdAlreadyInUseException iAiUe){
			log.warn("Node already use : {} : {}", name, iAiUe.getMessage(), iAiUe);
			return false;
		}
		
		if(null != label)
			labelErrorTreatment(label, n);
		
		this.finalStates.add(name);
		
		
		return true;
	}

	private void labelErrorTreatment(String label, Node n) {
		n.addAttribute(ATTR_LABEL, label);
		Boolean found = Arrays.asList(label.split(" ")).contains("ERROR:");
		if(found)
			n.setAttribute("ui.class", "error");
		found = Arrays.asList(label.split(" ")).contains("DONT_KNOW");
		if(found)
			n.setAttribute("ui.class", "unkown");
	}

	public boolean addState(String name, String label) {
		Node n = null;
		try {
			n = graph.addNode(name);
		} catch(IdAlreadyInUseException iAiUe){
			log.warn("Node already use : {} : {}", name, iAiUe.getMessage(), iAiUe);
			return false;
		}
		
		if(null != label){
			n.addAttribute(ATTR_LABEL, label);			
		}
		
		return true;
	}

	public boolean containsState(String name) {
		return graph.getNode(name) != null;
	}

	public void addTransition(String origin, String name, String label, String dest) throws StateNotFoundException {
		if(!containsState(origin)){
			throw new StateNotFoundException("Origin state not found : " + origin);
		}
		if(!containsState(dest)){
			throw new StateNotFoundException("Dest state not found : " + dest);
		}
		
		Edge e = null;
		try{
			e = graph.addEdge(name, origin, dest, true);
		}catch(EdgeRejectedException eRe){
			log.warn("Ege rejected between {} and {}, name {} : ", origin, dest, name, eRe);
		}
		if(e == null){
			log.debug("addEdge return null, name = " + name + " origin = " + origin + " dest = " + dest);
			return;
		}
		
		if(label != null){
			e.addAttribute(ATTR_LABEL, label);
		    
			if(this.side)
				e.setAttribute("ui.class", "side1");
			else
				e.setAttribute("ui.class", "side2");
			this.side = !this.side;
		}
	}
	
	public Graph getGraph(){
		return this.graph;
	}

	public boolean isFianlState(String name) {
		return this.finalStates.contains(name);
	}

	public String getIntialState() {
		return this.intialState.getId();
	}

}
