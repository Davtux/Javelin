package fr.unilim.concolic;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.Config;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Generates jpf.properties and Main.java files
 *
 */
public class FileGenerator {
	
	private static final Logger l = LoggerFactory.getLogger(FileGenerator.class);
	private static final String ENCODING = "UTF-8";
	
	private String appletClsName;
	private String packageName;
	
	
	private Configuration conf;
	
	public FileGenerator(String appletClassName, String packageName){
		appletClsName = appletClassName;
		this.packageName = packageName;
		
		conf = new Configuration(Configuration.VERSION_2_3_25);
		conf.setDefaultEncoding(ENCODING);
		try {
			conf.setDirectoryForTemplateLoading(new File("templates/"));
		} catch (IOException e) {
			l.error("Could not set templates directory: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Generates the jpf.properties file using the parameters passed in the constructor
	 * TODO: créer une liste pour récupérer puis traiter la liste des packages à inclure dans le fichier de config
	 * @return true if the generation was successful, false otherwise
	 */
	public boolean generateConfigFile(){
	
		try(PrintWriter writer = new PrintWriter(Config.JPF_CONF_NAME, ENCODING)) {
			Template tmpl = conf.getTemplate("jpf.properties");
		   	Map<String, Object> root = new HashMap<>();
		   	root.put("main_conf_name", "../"+Config.JPF_MAIN_CONF_NAME);
		   	root.put("packageName", packageName);
		   	root.put("appletClsName", appletClsName);
		   	root.put("apiJarPath", Config.getJavacardApiJarPath());
		    	
			tmpl.process(root, writer);
			
		} catch (TemplateException e) {
			l.error("An error occured while expanding the template: " + e.getMessage(), e);
		} catch (IOException e) {
			l.error("Could not create file config.jpf!", e);
		   return false;
		}
		l.info("File config.jpf created!");
		return true;
	}
	
	/**
	 * Generates a Main.java file to serve as a target for JDart
	 * @return true if the generation was successful, false otherwise
	 */
	public boolean generateMainFile(){
		try(PrintWriter writer = new PrintWriter(Config.TALOS_SRC_FOLDER+"MainTester.java", ENCODING);){
		    
		    Template tpl = conf.getTemplate("MainTester.java");
		    
		    	Map<String, Object> root = new HashMap<>();
		    	root.put("packageName", packageName);
		    	root.put("appletClsName", appletClsName);
		    	
		    	tpl.process(root, writer);
		} catch (TemplateException te) {
		    l.error("An error occured while expanding the template: " + te.getMessage(), te);
		} catch (IOException e) {
			l.error("Could not create file MainTester.java!", e);
		   return false;
		}
		l.info("File MainTester.java created!");
	    
	    return true;
	}

	public String getAppletClsName() {
		return appletClsName;
	}

	public void setAppletClsName(String appletClsName) {
		this.appletClsName = appletClsName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	

}
