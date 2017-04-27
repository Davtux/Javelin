package fr.unilim.filter.automaton;

import fr.unilim.automaton.models.IAutomaton;

public interface IAutomatonFilter {

	/**
	 * Apply filter on automaton.
	 * @param automaton
	 * @param result
	 */
	void doFilter(IAutomaton automaton, IAutomaton result);

	/**
	 * Get name's filter
	 * @return name's filter
	 */
	String getName();
}
