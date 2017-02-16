package fr.unilim;

import java.io.IOException;
import java.nio.file.Path;

/**
 * This class runs JPF with a certain properties file as parameter
 *
 */
public class JPFRunner {
	
	/**
	 * The path to the JPF executable on the host system
	 */
	private Path jpfExecutable;
	
	/**
	 * The Logger
	 */
	private Logger l;

	public JPFRunner(Path pathToJPF) {
		jpfExecutable = pathToJPF;
		l = new Logger(getClass().getName());
	}
	
	/**
	 * Runs JPF
	 * @param propertiesFiles the jpf.properties file to be passed as a parameter
	 */
	public void runJPF(Path propertiesFiles) {
		Runtime rt = Runtime.getRuntime();
		try {
			Process proc = rt.exec(new String[] { jpfExecutable.toString(), propertiesFiles.toString() });
			proc.waitFor();
		} catch (IOException e) {
			l.w(Logger.ERR, "Error launching JPF: " + e);
		} catch (InterruptedException e) {
			l.w(Logger.ERR, "Thread has been interrupted: " + e);
		}
	}
}
