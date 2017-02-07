package fr.unilim.automaton.graphstream.apdapter;

import java.util.HashSet;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import fr.unilim.automaton.models.IAutomaton;
import fr.unilim.automaton.models.StateNotFoundException;

public class AutomatonGraphml implements IAutomaton {
	
	public static final String ATTR_LABEL = "ui.label";
	
	private Graph graph;
	private Set<String> finalStates;
	private Node intialState;
	
	public AutomatonGraphml(String name){
		this.graph = new SingleGraph(name);
		this.finalStates = new HashSet<String>();
		this.intialState = this.graph.addNode("init");
	}

	public boolean addFinalState(String name, String label) {
		Node n = null;
		try {
			n = graph.addNode(name);
		} catch(IdAlreadyInUseException iAiUe){
			return false;
		}
		n.addAttribute(ATTR_LABEL, label);
		
		this.finalStates.add(name);
		
		return true;
	}

	public boolean addState(String name, String label) {
		Node n = null;
		try {
			n = graph.addNode(name);
		} catch(IdAlreadyInUseException iAiUe){
			return false;
		}
		n.addAttribute(ATTR_LABEL, label);
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
		Edge e = graph.addEdge(name, origin, dest, true);
		e.addAttribute(ATTR_LABEL, label);
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
