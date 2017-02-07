package fr.unilim.automaton.models;

/**
 * Interface define an automaton.
 */
public interface IAutomaton {
	
	/**
	 * Add final state.
	 * @param name
	 * @param label
	 */
	void addFinalState(String name, String label);
	

	/**
	 * Add state.
	 * @param name
	 * @param label
	 */
	void addState(String name, String label);
	
	/**
	 * Return true if state in automaton
	 * @param nane
	 * @return
	 */
	boolean containsState(String nane);
	
	/**
	 * Add transition between origin and dest.
	 * @param origin
	 * @param name
	 * @param label
	 * @param dest
	 * @throws StateNotFoundException if origin or dest not in automaton
	 */
	void addTransition(String origin, String name, String label, String dest) throws StateNotFoundException;
}
