package fr.unilim;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Logger {
	private String cls;
	private String fileName = "main.log";
	
	public static final int INFO = 0;
	public static final int WARN = 1;
	public static final int ERR = 2;

	
	public Logger(String cls){
		this.cls = cls;
		
		initDirNFile();		
	}
	
	private void initDirNFile(){
		new File("logs").mkdirs();
    	try {
    	      new File("logs/"+fileName).createNewFile();
        	} catch (IOException e) {
        		System.out.println("We got an IOException: "+ e.getMessage());
        		e.printStackTrace();
        	}
	}
	
	public void w(int lvl, String msg){
		String txt = format(lvl, msg);
		try {
		    Files.write(Paths.get("logs/"+fileName), txt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
	}
	
	private String format(int lvl, String msg){
		String f = "";
		String prefix = "";
		if(lvl == INFO){
			prefix = "(!) ";
		}else if(lvl == WARN){
			prefix = "[W] ";
		}else{
			prefix = "[E] ";
		}
		f = prefix + "{"+cls+"} : "+ msg +"\n";
		return f;
	}

}
