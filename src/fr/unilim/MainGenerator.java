package fr.unilim;

import java.io.FileWriter;

public class MainGenerator {
	
	private String classPath;
	private String newProjectPath;
	private String APDUClassName;
	
	public MainGenerator(String classPath, String newProjectPath, String APDUClassName) {
		this.classPath = classPath;
		this.newProjectPath = newProjectPath;
		this.APDUClassName = APDUClassName;
	}
	
	public void generate() {
		// TODO: Generate MainTest.java file and jpf.properties
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String getNewProjectPath() {
		return newProjectPath;
	}

	public void setNewProjectPath(String newProjectPath) {
		this.newProjectPath = newProjectPath;
	}

	public String getAPDUClassName() {
		return APDUClassName;
	}

	public void setAPDUClassName(String aPDUClassName) {
		APDUClassName = aPDUClassName;
	}
	
	
	
}
