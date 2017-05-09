package fr.unilim.tests;

import java.io.IOException;
import java.nio.file.Paths;

import fr.unilim.concolic.JavaCardProjectCompiler;
import fr.unilim.concolic.exception.CompileException;
import fr.unilim.concolic.exception.NoJDKException;
import fr.unilim.utils.os.UnsupportedOS;

public class MainTester {

	public static void main(String[] args) {
		try {
			JavaCardProjectCompiler.compile(Paths.get("/home/simon/dev/jdart-all/jdart/src/examples/fr/inria/lhs/tests/MyFutureApplet.java"), Paths.get("/tmp/"), Paths.get("/home/simon/dev/jdart-all/jdart/src/examples/"));
			System.out.println("Generated successfully");	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoJDKException e) {
			
		} catch (CompileException e) {
			e.printStackTrace();
		} catch (UnsupportedOS e) {
			e.printStackTrace();
		}
	}

}
