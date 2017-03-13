package fr.unilim;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.tools.*;

class NoJDKException extends Exception {
	public NoJDKException(String msg) {
		super(msg);
	}
}

public class JavaCardProjectCompiler {
	
	//@SuppressWarnings("restriction")
	public static boolean compile(File mainClassPath, File outputPath) throws IOException, NoJDKException {
		File[] files = { mainClassPath };

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			throw new NoJDKException("This version of Java is missing the JDK. Please install it to compile your project");
		}
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		
		String[] params = {
			"-d", outputPath.toString()
		};
		
		Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
		boolean ok = compiler.getTask(null, fileManager, null, Arrays.asList(params), null, compilationUnit).call();
		
		fileManager.close();
		
		return ok;
	}

}
