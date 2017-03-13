package fr.unilim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SUTIntegrator {
	
	private Logger l;
	
	private Path jDartPath;
	
	public SUTIntegrator(Path jDartPath){
		this.jDartPath = jDartPath;
		l = new Logger(getClass().getName());
	}

	/**
	 * Integrate the SUT project in the JDart structure by copying the .java and .class files
	 * @param srcPath Source folder (.java files) to be integrated in the jDart folder tree
	 * @param classPath Folder containing compiled files (.class) to be integrated in the jDart folder tree
	 */
	public boolean integrate(Path srcPath, Path classPath){
		File srcFolder = srcPath.toFile();
		File classFolder = classPath.toFile();
		File srcDest = new File(jDartPath.toString()+"/src/examples");
		File classDest = new File(jDartPath.toString()+"/build/examples");

    	//making sure source folder exists
    	if(!srcFolder.exists() || !classFolder.exists()){
    		l.w(Logger.ERR, "Directory does not exist.");
    		return false;

        }else{

           try{
        	copyFolder(srcFolder,srcDest);
        	l.w(Logger.INFO, "The sources files of the SUT project has been integrated in JDart successfully");
        	copyFolder(classFolder,classDest);
        	l.w(Logger.INFO, "The compiled files of the SUT project has been integrated in JDart successfully");
           }catch(IOException e){
        	   l.w(Logger.ERR, "Cannot integrate the SUT project in JDart");
        	   e.printStackTrace();
        	   return false;
           }
        }

    	l.w(Logger.INFO, "The SUT project has been integrated in JDart successfully");
    	return true;
	}
	
	/**
	 * Copy recursively every sub-folder and file in the source folder
	 * @param src Source folder to be copied
	 * @param dest Destination folder
	 * @throws IOException
	 */
	private void copyFolder(File src, File dest) throws IOException{
	    	if(src.isDirectory()){

	    		//if directory not exists, create it
	    		if(!dest.exists()){
	    		   dest.mkdir();
	    		   l.w(Logger.INFO, "Directory copied from "+ src + "  to " + dest);
	    		}

	    		//list all the directory contents
	    		String files[] = src.list();

	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}

	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest);

	    	        byte[] buffer = new byte[1024];

	    	        int length;
	    	        //copy the file content in bytes
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }

	    	        in.close();
	    	        out.close();
	    	        l.w(Logger.INFO, "File copied from " + src + " to " + dest);
	    	}
	    }

}
