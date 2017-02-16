package fr.unilim.automaton.algorithms;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.automaton.algorithms.exception.AlgorithmStateException;
import fr.unilim.automaton.models.IAutomaton;
import fr.unilim.automaton.models.StateNotFoundException;
import fr.unilim.automaton.utils.ConditionInverter;
import fr.unilim.counter.Counter;
import fr.unilim.tree.IBinaryTree;

/**
 * This class create {@link IAutomaton} from jdart's output (format JSON)
 */
public class AutomatonCreator {
	
	public static final String NAME_RESULT = "result";
	public static final String NAME_DECISION = "decision";
	
	private static final Logger log = LoggerFactory.getLogger(AutomatonCreator.class);
	
	private Counter counter;
	
	/**
	 * Constructor
	 */
	public AutomatonCreator(){
		this.counter = new Counter();
	}
	
	/**
	 * Parse JSON to {@link IAutomaton}
	 * @param inJson
	 * @return
	 * @throws IOException
	 * @throws ParseException 
	 * @throws AlgorithmStateException 
	 */
	public IAutomaton parse(IBinaryTree binaryTree, IAutomaton automaton) throws IOException, ParseException, AlgorithmStateException {
		this.counter.reset();
		try {
			constructAutomaton(binaryTree, automaton.getIntialState(), automaton);
		} catch (StateNotFoundException e) {
			throw new AlgorithmStateException("Eorro when construct automaton.", e);
		}
		
		return automaton;
	}
	
	private void constructAutomaton(IBinaryTree tree, String currentState, IAutomaton automaton) throws StateNotFoundException{
		if(automaton.isFianlState(currentState)){
			return;
		}
		
		String newState;
		IBinaryTree trueChild = tree.getLeft();
		
		if(trueChild.containsKey(NAME_RESULT)){
			newState = trueChild.getValues().get(NAME_RESULT);
			if(!automaton.containsState(newState)){
				automaton.addFinalState(newState, newState);
			}
		}else{
			newState = counter.nextString();
			automaton.addState(newState, "");
		}
		
		String decision = tree.getValues().get(NAME_DECISION);
		automaton.addTransition(currentState, counter.nextString(), decision, newState);
		constructAutomaton(trueChild, newState, automaton);
		
		IBinaryTree falseChild = tree.getRight();
		
		if(falseChild.containsKey(NAME_RESULT)){
			newState = falseChild.getValues().get(NAME_RESULT);
			if(!automaton.containsState(newState)){
				automaton.addFinalState(newState, newState);
			}
		}else{
			newState = counter.nextString();
			automaton.addState(newState, "");
		}
		
		automaton.addTransition(currentState, counter.nextString(), ConditionInverter.invertCondition(decision), newState); 
		constructAutomaton(falseChild, newState, automaton);
		
	}
	
}
