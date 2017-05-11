package fr.unilim.concolic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.Config;

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
	
	public JPFRunner() {
		jpfExecutable =  Paths.get(JPFConfigFileReader.getJPFPath() + "/" + Config.JPF_BIN);
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
			//Process proc = rt.exec(new String[] { jpfExecutable.toString(), propertiesFiles.toString() }, new String[] { "LD_LIBRARY_PATH="+z3Path});
			Process proc = rt.exec(new String[] 
					{"java", "-Xmx1024m", "-ea", "-jar",
							JPFConfigFileReader.getJPFPath() + Paths.get("/build/RunJPF.jar"),  propertiesFiles.toString()},
					new String[] { "LD_LIBRARY_PATH="+z3Path});
			
			BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(proc.getInputStream()));
			
			BufferedReader stdError = new BufferedReader(new 
			     InputStreamReader(proc.getErrorStream()));
			
			// read the output from the command
			l.info("Here is the standard output of JPF:\n");
			String s = null;
			while ((s = stdInput.readLine()) != null) {
			    l.info(s);
			}
			
			// read any errors from the attempted command
			l.info("Here is the standard error of JPF (if any):\n");
			while ((s = stdError.readLine()) != null) {
			    l.error(s);
			}

			proc.waitFor();
		} catch (IOException e) {
			l.error("Error launching JPF: ", e);
		} catch (InterruptedException e) {
			l.error("Thread has been interrupted: ", e);
		}
	}
	
	/*private String[] buildCmd(){
		String[] cmd = new String[];
		return cmd;
	}*/
}
