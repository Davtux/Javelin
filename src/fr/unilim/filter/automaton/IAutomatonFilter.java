package fr.unilim.filter.automaton;

import fr.unilim.automaton.models.IAutomaton;
import fr.unilim.filter.exception.FilterException;

public interface IAutomatonFilter {

	/**
	 * Apply filter on automaton.
	 * @param automaton
	 * @param result
	 * @throws FilterException 
	 */
	void doFilter(IAutomaton automaton, IAutomaton result) throws FilterException;

	/**
	 * Get name's filter
	 * @return name's filter
	 */
	String getName();
}
