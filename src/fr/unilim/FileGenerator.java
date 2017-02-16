package fr.unilim;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FileGenerator {
	
	private String clsPath;
	private String appletClsName;
	private String packageName;
	
	private Logger l;
	
	private Configuration conf;
	
	public FileGenerator(String classPath, String appletClassName, String packageName){
		clsPath = classPath;
		appletClsName = appletClassName;
		this.packageName = packageName;
		
		l = new Logger(getClass().getName());
		
		conf = new Configuration(Configuration.VERSION_2_3_25);
		conf.setDefaultEncoding("UTF-8");
		try {
			conf.setDirectoryForTemplateLoading(new File("templates/"));
		} catch (IOException e) {
			e.printStackTrace();
			l.w(Logger.ERR, "Could not set templates directory: " + e.getMessage());
		}
	}
	
	/**
	 * TODO: créer une liste pour récupérer puis traiter la liste des packages à inclure dans le fichier de config
	 * @return vrai si la génération s'est bien passée, faux sinon
	 */
	public boolean generateConfigFile(){
		try{
		    PrintWriter writer = new PrintWriter(Config.JPF_CONF_NAME, "UTF-8");
		    	
		    Template tmpl = conf.getTemplate("jpf.properties");
		    try {
		    	Map<String, Object> root = new HashMap<String, Object>();
		    	root.put("main_conf_name", Config.JPF_MAIN_CONF_NAME);
		    	root.put("packageName", packageName);
		    	root.put("appletClsName", appletClsName);
		    	
				tmpl.process(root, writer);
			} catch (TemplateException e) {
				e.printStackTrace();
				l.w(Logger.ERR, "An error occured while expanding the template: " + e.getMessage());
			} finally {
				writer.close();
			}
		    
		    l.w(Logger.INFO, "File config.jpf created!");
		    
		    return true;
		} catch (IOException e) {
			l.w(Logger.ERR, "Could not create file config.jpf!");
		   return false;
		}
	}

	public boolean generateMainFile(){
		try{
		    PrintWriter writer = new PrintWriter("Main.java", "UTF-8");
		    
		    Template tpl = conf.getTemplate("MainTester.java");
		    
		    try {
		    	Map<String, Object> root = new HashMap<String, Object>();
		    	root.put("packageName", packageName);
		    	root.put("appletClsName", appletClsName);
		    	
		    	tpl.process(root, writer);
		    } catch (TemplateException te) {
		    	te.printStackTrace();
		    	l.w(Logger.ERR, "An error occured while expanding the template: " + te.getMessage());
		    }
		    
		    writer.close();
		    l.w(Logger.INFO, "File Main.java created!");
		    
		    return true;
		} catch (IOException e) {
			l.w(Logger.ERR, "Could not create file Main.java!");
		   return false;
		}
	}

	public String getClsPath() {
		return clsPath;
	}

	public void setClsPath(String clsPath) {
		this.clsPath = clsPath;
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
