package fr.unilim;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class JPFConfigFileReader {
	
	private final static Path SITE_PROPERTIES = Paths.get(System.getProperty("user.home"), ".jpf", "site.properties");
	
	public static String getJPFPath() {
		Scanner in = null;
		try {
			in = new Scanner(new FileReader(SITE_PROPERTIES.toString()));
			while (in.hasNextLine()) {
				String line = in.nextLine();
				if (line.startsWith("jpf-core")) {
					String[] result = line.split("=");
					return result[1].trim();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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
				if (line.startsWith("jpf-jdart")) {
					String[] result = line.split("=");
					return result[1].trim();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
		}
		
		return null;
	}
	
}
