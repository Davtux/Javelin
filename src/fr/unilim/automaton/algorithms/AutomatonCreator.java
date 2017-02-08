package fr.unilim.automaton.algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.unilim.automaton.algorithms.exception.AlgorithmStateException;
import fr.unilim.automaton.models.IAutomaton;
import fr.unilim.automaton.models.StateNotFoundException;
import fr.unilim.automaton.utils.ConditionInverter;
import fr.unilim.automaton.utils.JDartOutputParser;
import fr.unilim.counter.Counter;

/**
 * This class create {@link IAutomaton} from jdart's output (format JSON)
 */
public class AutomatonCreator {
	
	public static final String CHARSET = "utf-8";

	private JSONParser jsonParser;
	private Counter counter;
	
	/**
	 * Constructor
	 */
	public AutomatonCreator(){
		this.jsonParser = new JSONParser();
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
	public IAutomaton parse(InputStream inJson, IAutomaton automaton) throws IOException, ParseException, AlgorithmStateException {
		String content = JDartOutputParser.parseJDartOutput(
			new BufferedReader(new InputStreamReader(inJson, CHARSET))
		);
		
		JSONObject json = (JSONObject) jsonParser.parse(content);
		
		this.counter.reset();
		try {
			constructAutomaton(json, automaton.getIntialState(), automaton);
		} catch (StateNotFoundException e) {
			throw new AlgorithmStateException("Eorro when construct automaton.", e);
		}
		
		return automaton;
	}
	
	private void constructAutomaton(JSONObject tree, String currentState, IAutomaton automaton) throws StateNotFoundException{
		if(automaton.isFianlState(currentState)){
			return;
		}
		
		String newState;
		JSONArray children = (JSONArray) tree.get("children");
		JSONObject trueObject = (JSONObject) children.get(0);
		
		if(trueObject.containsKey("result")){
			newState = trueObject.get("result").toString();
			if(!automaton.containsState(trueObject.get("result").toString())){
				automaton.addFinalState(newState, trueObject.get("result").toString());
			}
		}else{
			newState = counter.nextString();
			automaton.addState(newState, "");
		}
		
		String decision = tree.get("decision").toString();
		automaton.addTransition(currentState, counter.nextString(), decision, newState);
		constructAutomaton(trueObject, newState, automaton);
		
		JSONObject falsechild = (JSONObject) children.get(1);
		
		if(falsechild.containsKey("result")){
			newState = falsechild.get("result").toString();
			if(!automaton.containsState(falsechild.get("result").toString())){
				automaton.addFinalState(newState, falsechild.get("result").toString());			
			}
		}else{
			newState = counter.nextString();
			automaton.addState(newState, "");
		}
		
		automaton.addTransition(currentState, counter.nextString(), ConditionInverter.invertCondition(decision), newState); 
		constructAutomaton(falsechild, newState, automaton);
		
	}
	
}
