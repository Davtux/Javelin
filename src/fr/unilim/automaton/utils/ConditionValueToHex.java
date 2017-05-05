package fr.unilim.automaton.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.utils.BytesUtil;

public class ConditionValueToHex {
	
	private final static Logger logger = LoggerFactory.getLogger(ConditionValueToHex.class);
	
	private final static Pattern pattern = Pattern.compile("\\s(-{0,1}\\d+)\\b");

	private ConditionValueToHex() {}

	/**
	 * Transform value's condition 
	 * from decimal to hexadecimal.
	 * @param condition
	 * @return
	 */
	public static String transform(String condition){
		Matcher matcher = pattern.matcher(condition);
		String result = "";
		byte value;
		if(matcher.find() && matcher.groupCount() >= 1){
			try {
				value = Byte.parseByte(matcher.group(1));
				result = matcher.replaceFirst( " 0x" + BytesUtil.getHexaString(value).toUpperCase());
				result = ConditionValueToHex.transform(result);
			} catch (NumberFormatException e){
				logger.debug("{} : {} is not a byte", condition, matcher.group(1));
				result = condition;
			}
		}else{
			result = condition;
		}
		return result;
	}
}
