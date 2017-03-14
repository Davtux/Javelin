package fr.unilim;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;


public class JavaCardProjectCompiler {

	/**
	 * Compiles a given Java file using the system JDK
	 * @param mainClassPath Path to the Java file to be compiled
	 * @param outputPath Path where we'll put the *.class files
	 * @param packageTopLevel Path to the folder where the top-level package lives
	 * @return True if the compilation was successful, False otherwise
	 * @throws IOException If a file cannot be found
	 * @throws NoJDKException If we can't find a JDK on the system
	 */
	public static boolean compile(Path mainClassPath, Path outputPath, Path packageTopLevel) throws IOException, NoJDKException {
		File[] files = { mainClassPath.toFile() };

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			throw new NoJDKException("This version of Java is missing the JDK. Please install it to compile your project");
		}
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		
		String apiJar = Config.getJavacardApiJarPath();
		String annotationPath = Paths.get(JPFConfigFileReader.getJDartPath(), "src", "annotations").toString();
		String sourcepathParam = String.join(":", packageTopLevel.toString(), annotationPath);
		
		String[] params = {
			"-d", outputPath.toString(),
			"-cp", apiJar,
			"-sourcepath", sourcepathParam
		};
		
		Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
		boolean ok = compiler.getTask(null, fileManager, null, Arrays.asList(params), null, compilationUnit).call();
		
		fileManager.close();
		
		return ok;
	}

}
