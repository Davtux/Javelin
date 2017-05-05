package fr.unilim.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BytesUtil {

	/**
	 * Convert a short to two bytes
	 * @param src
	 * 		short convert
	 * @return byte array of 2 bytes.
	 */
	public static byte[] getBytes(short src) {
		byte[] ret = new byte[2];

		ret[1] = (byte) (src & 0xff);
		ret[0] = (byte) ((src >> 8) & 0xff);
		return ret;
	}
	
	public static byte getByte(String str){
		if(str.length() != 2){
			throw new IllegalArgumentException("String lenght must be equals to 2.");
		}
		Pattern pattern = Pattern.compile("^[0-9A-F]{2}$");
		Matcher matcher = pattern.matcher(str);
		if(!matcher.matches()){
			throw new IllegalArgumentException("Chars are in '0'..'9' or 'a'..'f' interval");
		}
		return (byte) Integer.parseInt(str, 16 );
	}
	
	public static byte[] getBytes(String str){
		String[] listStr = str.split(" ");
		byte[] result = new byte[listStr.length];
		for(int i = 0; i < listStr.length; i++)
			result[i] = getByte(listStr[i].trim());
		return result;
	}
	
	public static String getHexaString(byte b){
		int value = b;
		if(value < 0){
			value = (2 * Byte.MAX_VALUE + b) + 2;
		}
		String s = Integer.toHexString(value);
		if(s.length() == 1)
			s = "0" + s;
		return s;
	}

	public static String getHexaStrings(byte[] b){
		String str = new String();
		for(int i = 0; i < b.length; i++){
			str += getHexaString(b[i]) + " ";
		}
		str = str.trim();
		str = str.toUpperCase();
		return str;
	}
}
