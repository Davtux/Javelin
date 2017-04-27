package fr.unilim.filter.JSON;

import java.util.ArrayList;

public class JSONObject {
	protected String result;
	protected boolean branch;
	protected String decision;
	protected ArrayList<JSONObject> children;
	protected int lineNumber;
	
	public JSONObject(){
		children = new ArrayList<>();
	}
	
	public boolean isLeaf(){
		return (children.size()>0)?false:true;
	}
}
