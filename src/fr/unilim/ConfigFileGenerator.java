package fr.unilim;

import java.io.IOException;
import java.io.PrintWriter;

public class ConfigFileGenerator {
	
	public boolean generateFile(){
		try{
		    PrintWriter writer = new PrintWriter("config.jpf", "UTF-8");
		    	
		    writer.println("@include=config/main_config.jpf");
		    writer.println("target=fr.inria.lhs.tests.MainTester");
		    writer.println("jdart.configs.fr.inria.lhs.emvas.symbolic.include=fr.inria.lhs.emvas.*;fr.inria.lhs.tests.*");
		    writer.println("concolic.method.process=fr.inria.lhs.emvas.EMVApplet.process(apdu:fr.inria.lhs.tests.APDU)");
		    writer.println("concolic.method=process");
		    
		    writer.close();
		    
		    return true;
		} catch (IOException e) {
			Logger l = new Logger(getClass().getName());
			l.w(Logger.ERR, "Impossible de créer un fichier !");
		   return false;
		}
	}

}
