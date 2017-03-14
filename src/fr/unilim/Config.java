package fr.unilim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.utils.FileUtil;

public class Config {
	
	private static final Logger log = LoggerFactory.getLogger(Config.class);

	public static final String TALOS_SRC_FOLDER = "SUT/src/fr/unilim/talos/";
	public static final String SUT_SRC_FOLDER = "SUT/src/";
	public static final String SUT_BIN_FOLDER = "SUT/build/";
	public static final String LOG_DIR ="logs";
	public static final String JPF_CONF_NAME ="SUT/config.jpf";
	public static final String JPF_MAIN_CONF_NAME ="config/main_config.jpf";
	
	private static final String NAME_JAVACARD_API_JAR_PATH = "JAVACARD_API_JAR_PATH ";
	private static String javacardApiJarPath = "/home/simon/dev/OracleJCSDK/oracle_javacard_sdks/jc222_kit/lib/api.jar";

	private Config() {}
	
	
	public static String getJavacardApiJarPath() {
		return javacardApiJarPath;
	}
	
	/**
	 * Load configuration file
	 * @param filePath
	 * 
	 * @throws FileNotFoundException
	 * 
	 * @throws IOException 
	 * 	
	 */
	public static void loadConfigFile(String filePath) throws IOException{
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(filePath);
		prop.load(input);
		
		Config.javacardApiJarPath = prop.getProperty(
				Config.NAME_JAVACARD_API_JAR_PATH, 
				Config.javacardApiJarPath
				);
		
		FileUtil.closeFile(input);
	}
	
	/**
	 * Save configuration in configuration file
	 * @param filePath
	 * @throws IOException 
	 */
	public static void saveConfiguration(String filePath) throws IOException{
		Properties prop = new Properties();
		FileOutputStream out = new FileOutputStream(new File(filePath));
		
		prop.setProperty(Config.NAME_JAVACARD_API_JAR_PATH, Config.javacardApiJarPath);
		
		prop.store(out, "");
		
		FileUtil.closeFile(out);

		log.debug("Configuration file saved.");
	}
}
