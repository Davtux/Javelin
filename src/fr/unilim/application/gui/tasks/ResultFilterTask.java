package fr.unilim.application.gui.tasks;

import java.util.List;

import fr.unilim.automaton.models.IAutomaton;
import fr.unilim.filter.automaton.impl.ResultFilter;
import javafx.concurrent.Task;

public class ResultFilterTask extends Task<Void>{

	private List<String> results;
	private IAutomaton origin;
	private IAutomaton destination;
	
	@Override
	protected Void call() throws Exception {
		updateMessage("Running");
		ResultFilter filter = new ResultFilter("");
		for(String v : results){
			filter.setValueResult(v);
			filter.doFilter(origin, destination);
		}
		updateMessage("Done");
		return null;
	}

	public List<String> getResults() {
		return results;
	}

	public void setResults(List<String> results) {
		this.results = results;
	}

	public IAutomaton getOrigin() {
		return origin;
	}

	public void setOrigin(IAutomaton origin) {
		this.origin = origin;
	}

	public IAutomaton getDestination() {
		return destination;
	}

	public void setDestination(IAutomaton destination) {
		this.destination = destination;
	}
}
