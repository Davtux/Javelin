package fr.unilim.filter.automaton.impl;

import java.util.List;

import org.graphstream.graph.IdAlreadyInUseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.automaton.models.IAutomaton;
import fr.unilim.automaton.models.State;
import fr.unilim.automaton.models.StateNotFoundException;
import fr.unilim.automaton.models.Transition;
import fr.unilim.filter.automaton.IAutomatonFilter;

public class ResultFilter implements IAutomatonFilter {
	
	private final static Logger log = LoggerFactory.getLogger(ResultFilter.class);
	
	private String valueResult;

	public ResultFilter(String valueResult) {
		this.setValueResult(valueResult);
	}

	@Override
	public void doFilter(IAutomaton automaton, IAutomaton result) {
		List<Transition> transitions;
		result.addFinalState(automaton.getFinalState(valueResult));
		log.debug("Add final state : {}", automaton.getFinalState(valueResult) );
		try {
			walk(automaton, result, automaton.getFinalState(valueResult));
		} catch (StateNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void walk(IAutomaton automaton, IAutomaton result, State current) throws StateNotFoundException{
		if(current.equals(automaton.getIntialState())){
			return;
		}
		List<Transition> transitions = automaton.getTransitionsByDest(current.getName());
		for(Transition t : transitions){
			try {
				if(!t.getOrigin().equals(automaton.getIntialState())){
					log.debug("Add state : {}", t.getOrigin());
					result.addState(t.getOrigin());
				}
			} catch (IdAlreadyInUseException e) {
				log.info("State {} already exist, ignore", t.getOrigin().getName());
				continue;
			}
			try {
				log.debug("Add transtion : {}", t);				
				result.addTransition(t);
				walk(automaton, result, t.getOrigin());

			} catch (IdAlreadyInUseException e) {
				log.info("Transition {} already exist, ignore", t);
			}
		}
	}

	@Override
	public String getName() {
		return "Filter by result";
	}

	public String getValueResult() {
		return valueResult;
	}

	public void setValueResult(String valueResult) {
		this.valueResult = valueResult;
	}

}
