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

/**
 * Class managing the automaton.
 *
 */
public class AutomatonGraphml implements IAutomaton {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String ATTR_LABEL = "ui.label";
	public static final String ATTR_CLASS = "ui.class";
	
	private Graph graph;
	private Set<String> finalStates;
	private Node intialState;
	private boolean side;
	
	/**
	 * Automaton builder 
	 * @param name : name of the automaton
	 */
	public AutomatonGraphml(String name){
		this.graph = new SingleGraph(name); // MutliGraph for prevent EdgeRejetedException
		this.enableHQ();
		this.side = true;
		this.finalStates = new HashSet<>();
		this.intialState = this.graph.addNode("init");
		this.intialState.setAttribute(ATTR_CLASS, "initial");
		this.graph.addAttribute("ui.stylesheet", "url('graph.css')");
	}

	/**
	 * Enable anti-aliasing and HD
	 */
	private void enableHQ() {
		this.graph.addAttribute("ui.quality", true);
		this.graph.addAttribute("ui.antialias", true);
	}
	
	/**
	 * Disable anti-aliasing and HD
	 */
	public void disableHQ() {
		this.graph.removeAttribute("ui.quality");
		this.graph.removeAttribute("ui.antialias");
	}

	/**
	 * Add final state in the list.
	 * 
	 * @param name : name of the state
	 * @param label : label of the state
	 */
	@Override
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

	/**
	 * Apply css to Error and Unkonwn nodes
	 * 
	 * @param label : node's label
	 * @param n : node to treat
	 */
	private void labelErrorTreatment(String label, Node n) {
		n.addAttribute(ATTR_LABEL, label);
		Boolean found = Arrays.asList(label.split(" ")).contains("ERROR:");
		if(found)
			n.setAttribute(ATTR_CLASS, "error");
		else{
			found = Arrays.asList(label.split(" ")).contains("DONT_KNOW");
			if(found)
				n.setAttribute(ATTR_CLASS, "unkown");
			else
				n.setAttribute(ATTR_CLASS, "final");
		}
	}
	

	/**
	 * Add a state in the automaton
	 * 
	 * @param name : name of the state
	 * @param label : label of the state
	 */
	@Override
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

	@Override
	public boolean containsState(String name) {
		return graph.getNode(name) != null;
	}

	
	@Override
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
			log.debug("addEdge return null, name = {} origin = {} dest = {}", name, origin, dest);
			return;
		}
		
		if(label != null){
			e.addAttribute(ATTR_LABEL, label);
		    
			if(this.side)
				e.setAttribute(ATTR_CLASS, "side1");
			else
				e.setAttribute(ATTR_CLASS, "side2");
			this.side = !this.side;
		}
	}

	private void fixRejectedEdge(String origin, String name, String label, String dest) throws StateNotFoundException {
		String newDest = dest + "-1";
		if(null == this.graph.getNode(newDest)){
			log.info("Create new node {} : {}", dest, newDest);
			Node n = this.graph.getNode(dest);
			if(isFianlState(dest)){
				this.addFinalState(newDest, (String) n.getAttribute(ATTR_LABEL));
			}else{
				this.addState(newDest, (String) n.getAttribute(ATTR_LABEL));
			}
		}
		log.info("Create transition between {} and {}", origin, newDest);
		this.addTransition(origin, name, label, newDest);
	}
	
	public Graph getGraph(){
		return this.graph;
	}

	@Override
	public boolean isFianlState(String name) {
		return this.finalStates.contains(name);
	}

	@Override
	public String getIntialState() {
		return this.intialState.getId();
	}

}
