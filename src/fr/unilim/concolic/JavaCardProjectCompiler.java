package fr.unilim.concolic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.Config;
import fr.unilim.concolic.exception.CompileException;
import fr.unilim.concolic.exception.NoJDKException;
import fr.unilim.utils.os.OSValidator;
import fr.unilim.utils.os.UnsupportedOS;


public class JavaCardProjectCompiler {
	
	private final static Logger log = LoggerFactory.getLogger(JavaCardProjectCompiler.class);
	
	private JavaCardProjectCompiler(){}

	/**
	 * Compiles a given Java file using the system JDK
	 * @param mainClassPath Path to the Java file to be compiled
	 * @param outputPath Path where we'll put the *.class files
	 * @param packageTopLevel Path to the folder where the top-level package lives
	 * @return True if the compilation was successful, False otherwise
	 * @throws IOException If a file cannot be found
	 * @throws NoJDKException If we can't find a JDK on the system
	 * @throws CompileException If errors occured while compiling
	 * @throws UnsupportedOS  If os is unknowned
	 */
	public static void compile(Path mainClassPath, Path outputPath, Path packageTopLevel) throws IOException, NoJDKException, CompileException, UnsupportedOS {
		File[] files = { mainClassPath.toFile() };

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			throw new NoJDKException("This version of Java is missing the JDK. Please install it to compile your project");
		}
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		
		String apiJar = Config.getJavacardApiJarPath();
		String annotationPath = Paths.get(JPFConfigFileReader.getJDartPath(), "src", "annotations").toString();
		String sourcepathParam = String.join(getClasspathSeparator(), packageTopLevel.toString(), annotationPath);
		
		String[] params = {
			"-d", outputPath.toString(),
			"-cp", apiJar,
			"-sourcepath", sourcepathParam
		};
		
		DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<>();
		Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
		boolean ok = compiler.getTask(null, fileManager, diagnosticsCollector, Arrays.asList(params), null, compilationUnit).call();
		
		if (!ok) {
			StringBuilder errorStr = new StringBuilder();
	        List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticsCollector.getDiagnostics();
	        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
	            errorStr.append(diagnostic.getMessage(null) + "\n");
	        }
	        fileManager.close();
	        throw new CompileException(errorStr.toString());
	    }
		
		fileManager.close();
	}
	
	private static String getClasspathSeparator() throws UnsupportedOS{
		if(OSValidator.isUnix()){
			log.info("Detected os : is Unix");
			return ":";
		}
		if(OSValidator.isWindows()){
			log.info("Detected os : is Windows");
			return ";";
		}
		throw new UnsupportedOS("Unknown classpath sÚparator for os : " + OSValidator.OS);
	}

}
