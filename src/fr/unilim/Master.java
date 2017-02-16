package fr.unilim;

import java.nio.file.Path;

/**
 * Master class delegating file generation and running JPF to other classes
 *
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

	public Master(Path classPath, Path newProjectPath, String aPDUClassName, String packageName) {
		this.classPath = classPath;
		this.newProjectPath = newProjectPath;
		this.aPDUClassName = aPDUClassName;
		this.packageName = packageName;
	}
	
	/**
	 * Generates the MainTest.java file as well as jpf.properties 
	 */
	public void generate() {
		FileGenerator fg = new FileGenerator(classPath.toString(), aPDUClassName, packageName);
		fg.generateConfigFile();
		fg.generateMainFile();
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
