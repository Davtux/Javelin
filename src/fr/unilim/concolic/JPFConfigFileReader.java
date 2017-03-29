package fr.unilim.concolic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class JPFConfigFileReader {
	
	private final static Path SITE_PROPERTIES = Paths.get(System.getProperty("user.home"), ".jpf", "site.properties");
	
	public static String getJPFPath() {
		Scanner in = null;
		try {
			in = new Scanner(new FileReader(SITE_PROPERTIES.toString()));
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String result = getProperty("jpf-core", line);
				if (result != null) {
					return result;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
		}
		
		return null;
	}
	
	public static String getJDartPath() {
		Scanner in = null;
		try {
			in = new Scanner(new FileReader(SITE_PROPERTIES.toString()));
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String result = getProperty("jpf-jdart", line);
				if (result != null) {
					return result;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the property PROP in line LINE or returns null
	 * @param prop The property to get
	 * @param line The line to get it from
	 * @return The value of the property or null if it isn't on this line
	 */
	private static String getProperty(String prop, String line) {
		if (line.startsWith(prop)) {
			String[] result = line.split("=");
			return result[1].trim();
		}
		
		return null;
	}
	
}
