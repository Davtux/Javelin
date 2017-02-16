package fr.unilim;

import java.io.IOException;
import java.nio.file.Path;

/**
 * This class runs JPF with a certain properties file as parameter
 *
 */
public class JPFRunner {

	private Path jpfExecutable;
	private Logger l;

	public JPFRunner(Path pathToJPF) {
		jpfExecutable = pathToJPF;
		l = new Logger(getClass().getName());
	}

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
