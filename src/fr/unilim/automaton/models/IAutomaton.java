package fr.unilim.automaton.models;

import java.util.List;
import java.util.Set;

/**
 * Interface define an automaton.
 */
public interface IAutomaton {
	
	/**
	 * Add final state.
	 * @param name
	 * @param label
	 * @return False if state already exist.
	 */
	boolean addFinalState(String name, String label);
	boolean addFinalState(State state);
	

	/**
	 * Add state.
	 * @param name
	 * @param label
	 * @return False if state already exist.
	 */
	boolean addState(String name, String label);
	boolean addState(State state);
	
	/**
	 * Return true if state in automaton
	 * @param name
	 * @return
	 */
	boolean containsState(String name);
	
	/**
	 * Add transition between origin and dest.
	 * @param origin
	 * @param name
	 * @param label
	 * @param dest
	 * @throws StateNotFoundException if origin or dest not in automaton
	 */
	void addTransition(String origin, String name, String label, String dest) throws StateNotFoundException;
	void addTransition(Transition transition) throws StateNotFoundException;
	
	/**
	 * Return True if state is a final state.
	 * @param name
	 * @return
	 */
	boolean isFianlState(String name);
	
	List<Transition> getTransitionsByDest(String dest);
	
	List<Transition> getTransitionsByOrigin(String origin);
	
	/**
	 * Return initial state.
	 * @return
	 */
	State getIntialState();
	
	/**
	 * Return list of final states.
	 * @return
	 */
	List<State> getFinalStates();
	State getFinalState(String name);
}
