package fr.unilim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class runs JPF with a certain properties file as parameter
 *
 */
public class JPFRunner {
	
	/**
	 * The Logger
	 */
	private static final Logger l = LoggerFactory.getLogger(JPFRunner.class);
	
	/**
	 * The path to the JPF executable on the host system
	 */
	private Path jpfExecutable;
	
	public JPFRunner(Path pathToJPF) {
		jpfExecutable = pathToJPF;
	}
	
	/**
	 * Runs JPF
	 * @param propertiesFiles the jpf.properties file to be passed as a parameter
	 * @param z3Path the build folder of the z3 constraint solver
	 */
	public void runJPF(Path propertiesFiles, Path z3Path) {
		Runtime rt = Runtime.getRuntime();
		try {
			
			//String[] cmd = buildCmd();
			Process proc = rt.exec(new String[] { jpfExecutable.toString(), propertiesFiles.toString() }, new String[] { "LD_LIBRARY_PATH="+z3Path});

			BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(proc.getInputStream()));
			
			BufferedReader stdError = new BufferedReader(new 
			     InputStreamReader(proc.getErrorStream()));
			
			// read the output from the command
			System.out.println("Here is the standard output of JPF:\n");
			String s = null;
			while ((s = stdInput.readLine()) != null) {
			    System.out.println(s);
			}
			
			// read any errors from the attempted command
			System.out.println("Here is the standard error of JPF (if any):\n");
			while ((s = stdError.readLine()) != null) {
			    System.out.println(s);
			}

			proc.waitFor();
		} catch (IOException e) {
			l.error("Error launching JPF: " + e);
		} catch (InterruptedException e) {
			l.error("Thread has been interrupted: " + e);
		}
	}
	
	/*private String[] buildCmd(){
		String[] cmd = new String[];
		return cmd;
	}*/
}
