package fr.unilim;

import java.io.FileWriter;
import java.nio.file.Path;

public class MainGenerator {
	
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
	private String APDUClassName;
	
	/**
	 * The name of the package the APDU is in
	 */
	private String packageName;
	
	public MainGenerator(Path classPath, Path newProjectPath, String APDUClassName, String packageName) {
		this.classPath = classPath;
		this.newProjectPath = newProjectPath;
		this.APDUClassName = APDUClassName;
		this.packageName = packageName;
	}
	
	/**
	 * Generates the MainTest.java file as well as jpf.properties 
	 */
	public void generate() {
		// TODO: Generate MainTest.java file and jpf.properties
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
		return APDUClassName;
	}

	public void setAPDUClassName(String aPDUClassName) {
		APDUClassName = aPDUClassName;
	}
	
	
	
}
