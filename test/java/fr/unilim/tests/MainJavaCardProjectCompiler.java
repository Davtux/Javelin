package fr.unilim.tests;

import java.io.IOException;
import java.nio.file.Paths;

import fr.unilim.Config;
import fr.unilim.concolic.JPFConfigFileReader;
import fr.unilim.concolic.JavaCardProjectCompiler;
import fr.unilim.concolic.exception.CompileException;
import fr.unilim.concolic.exception.NoJDKException;
import fr.unilim.utils.os.UnsupportedOS;

public class MainJavaCardProjectCompiler {

	public static void main(String[] args) {
		try {
			Config.loadConfigFile("ProjetM1.conf");
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		try {
			JavaCardProjectCompiler.compile(
					Paths.get("test/resources/PorteMonnaie/src/fr/unilim/PorteMonnaie.java"), 
					Paths.get("/tmp/"), 
					Paths.get(JPFConfigFileReader.getJDartPath() + "/src/examples/"));

			System.out.println("Generated successfully");	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoJDKException e) {
			e.printStackTrace();
		} catch (CompileException e) {
			e.printStackTrace();
		} catch (UnsupportedOS e) {
			e.printStackTrace();
		}
	}
}
