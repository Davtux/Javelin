package fr.unilim.tests;

import java.io.IOException;
import java.nio.file.Paths;

import fr.unilim.Config;
import fr.unilim.JPFConfigFileReader;
import fr.unilim.JavaCardProjectCompiler;
import fr.unilim.NoJDKException;

public class MainJavaCardProjectCompiler {

	public static void main(String[] args) {
		try {
			Config.loadConfigFile("ProjetM1.conf");
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		boolean result;
		try {
			result = JavaCardProjectCompiler.compile(
					Paths.get("test/resources/PorteMonnaie/src/fr/unilim/PorteMonnaie.java"), 
					Paths.get("/tmp/"), 
					Paths.get(JPFConfigFileReader.getJDartPath() + "/src/examples/"));
			if (result) {
				System.out.println("Generated successfully");	
			} else {
				System.out.println("Error compiling");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoJDKException e) {
			e.printStackTrace();
		}
	}
}
