package fr.unilim.concolic;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.Config;
import fr.unilim.concolic.exception.CompileException;
import fr.unilim.concolic.exception.ConcolicExecutionException;
import fr.unilim.concolic.exception.NoJDKException;
import fr.unilim.utils.os.UnsupportedOS;

/**
 * Master class delegating file generation and running JPF to other classes
 * This class should be the only interface with the main class,
 * This represents an abstraction layer
 */
public class Master {
	
	/**
	 * The Logger
	 */
	private static final Logger l = LoggerFactory.getLogger(Master.class);
	
	/**
	 * The Path where the project under test is located
	 */
	private Path projectPath;
	
	/**
	 * The name of the class containing the APDU 
	 */
	private String aPDUClassName;
	/**
	 * The name of the package the APDU is in
	 */
	private String packageName;
	

	public Master(Path projectPath, String aPDUClassName, String packageName) {
		this.projectPath = projectPath;
		this.aPDUClassName = aPDUClassName;
		this.packageName = packageName;
		l.info("Start: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
	}
	
	/**
	 * Generates the MainTest.java file as well as jpf.properties 
	 * @return true Only when both config and the main files are generated successfully
	 */
	private boolean generate() {
		FileGenerator fg = new FileGenerator(aPDUClassName, packageName);
		return fg.generateConfigFile() && fg.generateMainFile();
	}
	
	/**
	 * Instantiate JPFRunner class in order to execute JPF
	 * @param pathToJPF The path to the JPF executable on the host system
	 * @param z3Path the build folder of the z3 constraint solver
	 * 
	 * @return returns true if no error detected otherwise false with a log error and an exception
	 */
	public boolean execute(Path z3Path) throws ConcolicExecutionException{
		String jDart = JPFConfigFileReader.getJDartPath();
		Path jDartPath = Paths.get(jDart);
		SUTIntegrator integrator = new SUTIntegrator(jDartPath);
		if(generate() && integrator.getSrc(projectPath)){
			try {
				JavaCardProjectCompiler.compile(Paths.get(Config.TALOS_SRC_FOLDER+"MainTester.java"), Paths.get(Config.SUT_BIN_FOLDER), Paths.get("SUT", "src"));
				l.info("SUT compiled successfully.");
				if(jDart != null){
					if(integrator.integrate(Paths.get("SUT", "src"), Paths.get("SUT", "build"))){
						JPFRunner jpfRunner = new JPFRunner();
						l.info("JPF running...");
						jpfRunner.runJPF(Paths.get(Config.JPF_CONF_NAME), z3Path);	
					}else{
						l.error("Failed to integrate the SUT into JDart structor.");
						throw new ConcolicExecutionException("Failed to integrate the SUT into JDart structor.");
					}
				}else{
					l.error("Cannot find JDart path.");
					throw new ConcolicExecutionException("Cannot find JDart path.");
				}
			} catch(CompileException e){
					l.error("Errors occured while compiling the SUT.", e);
					throw new ConcolicExecutionException("Errors occured while compiling the SUT.", e);
			} catch (IOException | NoJDKException e) {
				l.error("Cannot compile SUT.", e);
				throw new ConcolicExecutionException("Cannot compile SUT : " + e.getMessage(), e);
			} catch (UnsupportedOS e) {
				l.error(e.getMessage(), e);
				throw new ConcolicExecutionException("Cannot compile SUT : " + e.getMessage(), e);
			}
		}else{
			l.error("Failed when generating files.");
			throw new ConcolicExecutionException("Cannot generate files");
		}
		return true;
	}

	public Path getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(Path projectPath) {
		this.projectPath = projectPath;
	}

	public String getAPDUClassName() {
		return aPDUClassName;
	}

	public void setAPDUClassName(String aPDUClassName) {
		this.aPDUClassName = aPDUClassName;
	}
	
	
	
}
