package fr.unilim.automaton.graphstream.apdapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
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
	
	public AutomatonGraphml(String name){
		this.graph = new SingleGraph(name); // MutliGraph for prevent EdgeRejetedException
		this.enableHQ();
		this.finalStates = new HashSet<String>();
		this.intialState = this.graph.addNode("init");
		this.intialState.addAttribute("ui.style", "fill-color: rgb(0,50,255);");
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
		Boolean found = Arrays.asList(label.split(" ")).contains("ERROR:")|| Arrays.asList(label.split(" ")).contains("DONT_KNOW");
		if(found)
			n.addAttribute("ui.style", "fill-color: rgb(255,0,50);");
		else
			n.addAttribute("ui.style", "fill-color: rgb(0,255,50);");
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
			fixRejectedEdge(origin, name, label, dest);
			return;
		}
		if(e == null){
			log.debug("addEdge return null, name = " + name + " origin = " + origin + " dest = " + dest);
			return;
		}
		
		if(label != null){
			e.addAttribute(ATTR_LABEL, label);	
			e.addAttribute("ui.style", "text-offset: -1000, -200;"); 
		}
	}

	private void fixRejectedEdge(String origin, String name, String label, String dest) throws StateNotFoundException {
		String newDest = dest + "-1";
		if(null == this.graph.getNode(newDest)){
			log.info("Create new node {} : {}", dest, newDest);
			Node n = this.graph.getNode(dest);
			if(isFianlState(dest)){
				this.addFinalState(newDest, (String) n.getAttribute("ui.label"));
			}else{
				this.addState(newDest, (String) n.getAttribute("ui.label"));
			}
		}
		log.info("Create transition between {} and {}", origin, newDest);
		this.addTransition(origin, name, label, newDest);
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
