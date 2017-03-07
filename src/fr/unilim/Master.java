package fr.unilim;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Master class delegating file generation and running JPF to other classes
 * This class should be the only interface with the main class,
 * This represents an abstraction layer
 */
public class Master {
	
	/**
	 * A Path pointing to the location of the .class files we're testing
	 */
	private Path classPath;
	
	/**
	 * The Path were we'll create the project used by JDart to test the applet
	 */
	private Path newProjectPath;
	
	/**
	 * The name of the class containing the APDU 
	 */
	private String aPDUClassName;
	/**
	 * The name of the package the APDU is in
	 */
	private String packageName;
	
	/**
	 * The Logger
	 */
	private Logger l;

	public Master(Path classPath, Path newProjectPath, String aPDUClassName, String packageName) {
		this.classPath = classPath;
		this.newProjectPath = newProjectPath;
		this.aPDUClassName = aPDUClassName;
		this.packageName = packageName;
		l = new Logger(getClass().getName());
		l.w(Logger.INFO, "Start: "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
	}
	
	/**
	 * Generates the MainTest.java file as well as jpf.properties 
	 * @return true Only when both config and the main files are generated successfully
	 */
	private boolean generate() {
		FileGenerator fg = new FileGenerator(classPath.toString(), aPDUClassName, packageName);
		return fg.generateConfigFile() && fg.generateMainFile();
	}
	
	/**
	 * Instantiate JPFRunner class in order to execute JPF
	 * @param pathToJPF The path to the JPF executable on the host system
	 */
	public void execute(Path pathToJPF){
		if(generate()){
			JPFRunner jpfRunner = new JPFRunner(pathToJPF);
			jpfRunner.runJPF(Paths.get(Config.JPF_CONF_NAME));
		}else{
			l.w(Logger.ERR, "Failed when generating files.");
		}
	}

	public Path getClassPath() {
		return classPath;
	}

	public void setClassPath(Path classPath) {
		this.classPath = classPath;
	}

	public Path getNewProjectPath() {
		return newProjectPath;
	}

	public void setNewProjectPath(Path newProjectPath) {
		this.newProjectPath = newProjectPath;
	}

	public String getAPDUClassName() {
		return aPDUClassName;
	}

	public void setAPDUClassName(String aPDUClassName) {
		this.aPDUClassName = aPDUClassName;
	}
	
	
	
}
