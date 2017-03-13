package fr.unilim;

import java.io.IOException;
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
	 * @param z3Path the build folder of the z3 constraint solver
	 */
	public void execute(Path pathToJPF, Path z3Path){
		if(generate()){
			try {
				boolean compilationState = JavaCardProjectCompiler.compile(Paths.get(Config.SUT_SRC_FOLDER+"MainTester.java"), Paths.get(Config.SUT_BIN_FOLDER));
				if(compilationState)
					l.w(Logger.INFO, "SUT compiled successfully.");
				else
					l.w(Logger.WARN, "Errors occured while compiling the SUT.");
			} catch (IOException | NoJDKException e) {
				l.w(Logger.ERR, "Cannot compile SUT.");
				e.printStackTrace();
			}
			
			String jDart = JPFConfigFileReader.getJDartPath();
			if(jDart != null){
				Path jDartPath = Paths.get(jDart);
				
				SUTIntegrator integrator = new SUTIntegrator(jDartPath);
				
				if(integrator.integrate(Paths.get("SUT", "src"), Paths.get("SUT", "build"))){
					JPFRunner jpfRunner = new JPFRunner(pathToJPF);
					jpfRunner.runJPF(Paths.get(Config.JPF_CONF_NAME), z3Path);		
				}else{
					l.w(Logger.ERR, "Failed to integrate the SUT into JDart structor.");
				}
			}else{
				l.w(Logger.ERR, "Cannot find JDart path.");
			}
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
