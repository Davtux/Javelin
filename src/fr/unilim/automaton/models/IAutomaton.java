package fr.unilim.automaton.models;

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
	

	/**
	 * Add state.
	 * @param name
	 * @param label
	 * @return False if state already exist.
	 */
	boolean addState(String name, String label);
	
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
	
	/**
	 * Return True if state is a final state.
	 * @param name
	 * @return
	 */
	boolean isFianlState(String name);
	
	/**
	 * Return name of initial state.
	 * @return
	 */
	String getIntialState();
}
