package fr.unilim.automaton.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class ConditionInverter {

	static final String AND = "&&";
	static final String OR_ESCAPE = "\\|\\|";
	static final String OR = "||";
	static final String NO = "!";
	static final String GT = ">";
	static final String LT = "<";
	static final String EQ = "==";
	static final String NEQ = "!=";
	static final String LE = "<=";
	static final String GE = ">=";
	
	static final HashMap<String, String> INVERSE = new LinkedHashMap<>();
	static{
	 INVERSE.put(AND, OR);
	 INVERSE.put(OR_ESCAPE, AND);
	 INVERSE.put(LE, GT);
	 INVERSE.put(GE, LT);
	 INVERSE.put(GT, LE);
	 INVERSE.put(LT, GE);
	 INVERSE.put(EQ, NEQ);
	 INVERSE.put(NEQ, EQ);
	 INVERSE.put(NO, "");
	}
	
	private ConditionInverter(){}
	
	/**
	 * 
	 * @param conditionToInvert
	 * @return
	 */
	public static String invertCondition(String conditionToInvert){
		String[] splitedPartsAND;
		String[] splitedPartsOR;
		splitedPartsAND = conditionToInvert.split(AND);
		StringBuilder toReturn = new StringBuilder();
		String sOR;
		String sAND; 
		for(int i =0; i<splitedPartsAND.length; i++){
			sAND = splitedPartsAND[i];
			splitedPartsOR = sAND.split(OR_ESCAPE);
			for(int j = 0; j < splitedPartsOR.length; j++){
				sOR = stripWhitespace(splitedPartsOR[j]);
				if(j != splitedPartsOR.length -1)
					toReturn.append(invertAtomicCondition(sOR) + " " + AND + " ");
				else toReturn.append(invertAtomicCondition(sOR));	
			}
			if( i!= splitedPartsAND.length-1)
				toReturn.append(" " + OR + " ");
		}	
		return toReturn.toString();
	}
	
	/**
	 * 
	 * @param conditionToInvert
	 * @return
	 */
	private static String invertAtomicCondition(String conditionToInvert){
		String converted;
		for(Entry<String, String> entry : INVERSE.entrySet()){
			converted = conditionToInvert.replaceFirst(entry.getKey(), entry.getValue());
			if(!converted.equals(conditionToInvert))
				return converted;
		}
		return "!"+conditionToInvert;
	}
	
	private static String stripWhitespace(String str){
		return str.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
	}
}
