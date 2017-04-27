package fr.unilim.filter.JSON;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONFilter {
	
	private Gson gson;
	private JSONObject root;
	
	public JSONFilter(String json){
		GsonBuilder builder = new GsonBuilder();
		gson = builder.create();
		root = gson.fromJson(json, JSONObject.class);
	}
	
	public String filterByDecision(String query){
		ArrayList<JSONObject> collection = new ArrayList<>();
		doDecisionFilter(root, collection, query);
		if(!collection.isEmpty()){
			return gson.toJson(collection);
		}
		return "{}";
	}
	
	public String filterByResult(String query){
		ArrayList<JSONObject> collection = new ArrayList<>();
		doResultFilter(root, collection, query);
		if(!collection.isEmpty()){
			return gson.toJson(collection);
		}
		return "{}";
	}
	
	private void doDecisionFilter(JSONObject r, ArrayList<JSONObject> collection, String query){
		if(!r.isLeaf()){
			for(JSONObject child: r.children){
				if(child.decision != null && child.decision.contains(query)){
					collection.add(child);
				}
				doDecisionFilter(child, collection, query);
			}
		}else if(r.decision != null && r.decision.contains(query)){
			collection.add(r);
		}
	}
	
	private void doResultFilter(JSONObject r, ArrayList<JSONObject> collection, String query){
		if(!r.isLeaf()){
			for(JSONObject child: r.children){
				if(child.result != null && child.result.contains(query)){
					collection.add(child);
				}
				doDecisionFilter(child, collection, query);
			}
		}else if(r.result != null && r.result.contains(query)){
			collection.add(r);
		}
	}
}
