package fr.unilim.automaton.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ConditionInverter {

	final static String AND = "&&";
	final static String OR_ESCAPE = "\\|\\|";
	final static String OR = "||";
	final static String NO = "!";
	final static String GT = ">";
	final static String LT = "<";
	final static String EQ = "==";
	final static String NEQ = "!=";
	final static String LE = "<=";
	final static String GE = ">=";
	
	final static HashMap<String, String> INVERSE = new LinkedHashMap<String, String>();
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
		String[] splitedPartsAND, splitedPartsOR;
		splitedPartsAND = conditionToInvert.split(AND);
		String toReturn = new String();
		String sOR, sAND; 
		for(int i =0; i<splitedPartsAND.length; i++){
			sAND = splitedPartsAND[i];
			splitedPartsOR = sAND.split(OR_ESCAPE);
			for(int j = 0; j < splitedPartsOR.length; j++){
				sOR = stripWhitespace(splitedPartsOR[j]);
				if(j != splitedPartsOR.length -1)
					toReturn += invertAtomicCondition(sOR) + " " + AND + " ";
				else toReturn += invertAtomicCondition(sOR);	
			}
			if( i!= splitedPartsAND.length-1)
				toReturn  += " " + OR + " ";
		}	
		return toReturn;
	}
	
	/**
	 * 
	 * @param conditionToInvert
	 * @return
	 */
	private static String invertAtomicCondition(String conditionToInvert){
		String converted;
		for(String s : INVERSE.keySet()){
			converted = conditionToInvert.replaceAll(s, INVERSE.get(s));
			if(!converted.equals(conditionToInvert))
				return converted;
		}
		return "!"+conditionToInvert;
	}
	
	private static String stripWhitespace(String str){
		return str.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
	}
}
