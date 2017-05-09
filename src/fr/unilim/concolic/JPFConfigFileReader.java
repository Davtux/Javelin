package fr.unilim.concolic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPFConfigFileReader {
	
	private static final Logger log = LoggerFactory.getLogger(JPFConfigFileReader.class);
	
	private static final Path SITE_PROPERTIES = Paths.get(System.getProperty("user.home"), ".jpf", "site.properties");
	
	private JPFConfigFileReader(){}
	
	public static String getJPFPath() {
		try(Scanner in = new Scanner(new FileReader(SITE_PROPERTIES.toString()));) {
			
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String result = getProperty("jpf-core", line);
				if (result != null) {
					result = setUserHome(result);
					return result;
				}
			}
		} catch (FileNotFoundException e) {
			log.error("Not found file", e);
		}
		
		return null;
	}
	
	public static String getJDartPath() {
		try(Scanner in = new Scanner(new FileReader(SITE_PROPERTIES.toString()));) {
			
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String result = getProperty("jpf-jdart", line);
				if (result != null) {
					result = setUserHome(result);
					return result;
				}
			}
		} catch (FileNotFoundException e) {
			log.error("Not found file", e);
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
	
	/**
	 * Replace in properties : ${user.home} by System.properties("user.home");
	 * @param prop
	 * @return properties modify.
	 */
	private static String setUserHome(String prop){
		return prop.replace("${user.home}", System.getProperty("user.home"));
	}
	
}
