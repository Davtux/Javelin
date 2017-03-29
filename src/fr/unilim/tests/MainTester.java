package fr.unilim.tests;

import java.io.IOException;
import java.nio.file.Paths;

import fr.unilim.concolic.JavaCardProjectCompiler;
import fr.unilim.concolic.Master;
import fr.unilim.concolic.NoJDKException;

public class MainTester {

	public static void main(String[] args) {
		boolean result;
		try {
			result = JavaCardProjectCompiler.compile(Paths.get("/home/simon/dev/jdart-all/jdart/src/examples/fr/inria/lhs/tests/MyFutureApplet.java"), Paths.get("/tmp/"), Paths.get("/home/simon/dev/jdart-all/jdart/src/examples/"));
			if (result) {
				System.out.println("Generated successfully");	
			} else {
				System.out.println("Error compiling");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoJDKException e) {
			
		}
	}

}
