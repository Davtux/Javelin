package fr.unilim.tree.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.unilim.automaton.utils.JDartOutputParser;
import fr.unilim.tree.IBinaryTree;

public class BinaryTreeJSON implements IBinaryTree {
	
	public static final String CHARSET = "utf-8";
	
	public static final String[] nameValues = {"decision", "result"};
	
	private JSONParser jsonParser;
	
	private BinaryTreeJSON left;
	private BinaryTreeJSON right;
	private Map<String, String> values;
	
	/**
	 * Construct {@link IBinaryTree} from JDart JSON output.
	 * @param inJson
	 * @throws IOException
	 * @throws ParseException
	 */
	public BinaryTreeJSON(InputStream inJson) throws IOException, ParseException{
		this.jsonParser = new JSONParser();
		
		String content = JDartOutputParser.parseJDartOutput(
			new BufferedReader(new InputStreamReader(inJson, CHARSET))
		);
			
		JSONObject node = (JSONObject) jsonParser.parse(content);
		this.parse(node);
	}
	
	protected BinaryTreeJSON(JSONObject node){
		this.parse(node);
	}
	
	protected void parse(JSONObject node){
		this.left = null;
		this.right = null;
		
		JSONArray children = (JSONArray) node.get("children");
		if(null != children){
			JSONObject trueObject = (JSONObject) children.get(0);
			this.left = new BinaryTreeJSON(trueObject);
			
			JSONObject falseObject = (JSONObject) children.get(1);
			this.right = new BinaryTreeJSON(falseObject);
		}
		
		this.values = new HashMap<>();
		for(String n : nameValues){
			if(node.containsKey(n)){
				values.put(n, (String) node.get(n));
			}
		}
	}

	@Override
	public IBinaryTree getLeft() {
		return this.left;
	}

	@Override
	public IBinaryTree getRight() {
		return this.right;
	}

	@Override
	public Map<String, String> getValues() {
		return this.values;
	}
	
	@Override
	public boolean containsKey(String key) {
		return this.values.containsKey(key);
	}

}
