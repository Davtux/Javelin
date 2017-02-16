package fr.unilim;

import java.io.IOException;
import java.nio.file.Path;

/**
 * This class runs JPF with a certain properties file as parameter
 *
 */
public class JPFRunner {

	private Path jpfExecutable;

	public JPFRunner(Path pathToJPF) {
		jpfExecutable = pathToJPF;
	}

	public void runJPF(Path propertiesFiles) {
		Runtime rt = Runtime.getRuntime();
		try {
			Process proc = rt.exec(new String[] { jpfExecutable.toString(), propertiesFiles.toString() });
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
