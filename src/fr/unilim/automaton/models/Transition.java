package fr.unilim.automaton.models;

public class Transition {

	private State origin;
	private State dest;
	
	private String name;
	private String label;
	
	public Transition(State origin, State dest, String name, String label) {
		super();
		this.origin = origin;
		this.dest = dest;
		this.name = name;
		this.label = label;
	}

	public State getOrigin() {
		return origin;
	}

	public void setOrigin(State origin) {
		this.origin = origin;
	}

	public State getDest() {
		return dest;
	}

	public void setDest(State dest) {
		this.dest = dest;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "Transition [origin=" + origin + ", dest=" + dest + ", name=" + name + ", label=" + label + "]";
	}
	
}
