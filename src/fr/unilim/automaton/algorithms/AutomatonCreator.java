package fr.unilim.automaton.algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.unilim.automaton.models.IAutomaton;
import fr.unilim.automaton.models.StateNotFoundException;
import fr.unilim.automaton.utils.JDartOutputParser;

/**
 * This class create {@link IAutomaton} from jdart's output (format JSON)
 */
public class AutomatonCreator {
	
	public static final String CHARSET = "utf-8";

	private JSONParser jsonParser;
	private Integer counter;
	
	/**
	 * Constructor
	 */
	public AutomatonCreator(){
		this.jsonParser = new JSONParser();
	}
	
	/**
	 * Parse JSON to {@link IAutomaton}
	 * @param inJson
	 * @return
	 * @throws IOException
	 * @throws ParseException 
	 */
	public IAutomaton parse(InputStream inJson, IAutomaton automaton) throws IOException, ParseException {
		String content = JDartOutputParser.parseJDartOutput(
			new BufferedReader(new InputStreamReader(inJson, CHARSET))
		);
		
		JSONObject json = (JSONObject) jsonParser.parse(content);
		
		this.counter = 0;
		constructAutomaton(json, automaton.getIntialState(), automaton);
		
		return automaton;
	}
	
	private void constructAutomaton(JSONObject tree, String currentState, IAutomaton automaton){
		if(automaton.isFianlState(currentState)){
			return;
		}
		
		String newState;
		JSONArray children = (JSONArray) tree.get("children");
		JSONObject trueObject = (JSONObject) children.get(0);
		
		if(trueObject.containsKey("result") && !automaton.containsState(trueObject.get("result").toString())){
			newState = counter.toString();
			counter++;
			automaton.addFinalState(newState, trueObject.get("result").toString());
		}else if(trueObject.containsKey("result")){
			newState = trueObject.get("result").toString();
		}else{
			newState = counter.toString();
			counter++;
			automaton.addState(newState, "");
		}
		
		try {
			automaton.addTransition(currentState, counter.toString(), tree.get("decision").toString(), newState);
		} catch (StateNotFoundException e) {
			e.printStackTrace();
			return;
		}
		counter++;
		constructAutomaton(trueObject, newState, automaton);
		
		JSONObject falsechild = (JSONObject) children.get(1);
		
		if(falsechild.containsKey("result") && !automaton.containsState(falsechild.get("result").toString())){
			newState = counter.toString();
			counter++;
			automaton.addFinalState(newState, falsechild.get("result").toString());
		}else if(falsechild.containsKey("result")){
			newState = falsechild.get("result").toString();
		}else{
			newState = counter.toString();
			counter++;
			automaton.addState(newState, "");
		}
		
		try {
			automaton.addTransition(currentState, counter.toString(), "INVERSE", newState);
		} catch (StateNotFoundException e) {
			e.printStackTrace();
			return;
		}
		counter++;
		constructAutomaton(falsechild, newState, automaton);
		
	}
	
}
