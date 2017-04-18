package fr.unilim.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
	
	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
	
	private FileUtil(){}

	/**
	 * Close a Closeable Object (as File)
	 * @param file
	 */
	public static void closeFile(Closeable file) {
		try {
			if(file != null)
				file.close();
		} catch (final Exception e) {
			log.warn("Can't close file : {}", e.getMessage(), e);
		}
	}
	
	/**
	 * Give list of files in sub directories
	 * @param dir
	 * @return list of files
	 */
	public static List<File> getFilesInSubDir(File dir){
		List<File> files = new ArrayList<>();
		List<File> temp = new ArrayList<>(Arrays.asList(dir.listFiles()));
		File currentFile;
		
		while(!temp.isEmpty()){
			currentFile = temp.get(0);
			temp.remove(0);
			if(currentFile.isDirectory()){
				temp.addAll(Arrays.asList(currentFile.listFiles()));
			}else{
				files.add(currentFile);
			}
		}
		return files;
	}
	
	public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        	return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
