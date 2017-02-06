package fr.unilim;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

public class FileGenerator {
	
	private String clsPath;
	private String appletClsName;
	private String packageName;
	
	private Logger l;
	
	public FileGenerator(String classPath, String appletClassName, String packageName){
		clsPath = classPath;
		appletClsName = appletClassName;
		this.packageName = packageName;
		
		l = new Logger(getClass().getName());
	}
	
	/**
	 * TODO: créer une liste pour récupérer puis traiter la liste des packages à inclure dans le fichier de config
	 * @return
	 */
	public boolean generateConfigFile(){
		try{
		    PrintWriter writer = new PrintWriter(Config.JPF_CONF_NAME, "UTF-8");
		    	
		    writer.println("@include="+Config.JPF_MAIN_CONF_NAME);
		    writer.println("target=fr.unilim.???.MainTester");
		    writer.println("jdart.configs."+packageName+".symbolic.include="+packageName+".*;");
		    writer.println("concolic.method.process="+packageName+"."+appletClsName+".process(apdu:fr.unilim.jc.APDU)");
		    writer.println("concolic.method=process");
		    
		    writer.close();
		    
		    l.w(Logger.INFO, "Le fichier config.jpf est bien créé !");
		    
		    return true;
		} catch (IOException e) {
			l.w(Logger.ERR, "Impossible de créer un fichier !");
		   return false;
		}
	}
	/* TODO: créer le fichier :p */
	public boolean generateMainFile(){
		try{
		    PrintWriter writer = new PrintWriter("Main.java", "UTF-8");
		    /*
		     * package fr.inria.lhs.tests;

import fr.inria.lhs.tests.APDU;
import javacard.framework.ISOException;
import lhs.simu.jc.JCRE;

public class MainTester {

	public static void main(String[] args) {
		System.out.println("+ In main! +");

		
		new JCRE();	//we init the run env for JavaCard
		
		APDU mAPDU = new APDU();
		
		//byte[] theBuffer = {MyFutureApplet.CLA_TESTAPPLET, MyFutureApplet.INS_INTERROGER_COMPTEUR, (byte) 0, (byte) 0, (byte) 5};
		//run(new MyFutureApplet(), mAPDU);
		
		run(new fr.inria.lhs.emvas.EMVApplet(), mAPDU);


	}
	
	public static void run(fr.inria.lhs.emvas.EMVApplet app, APDU apdu){
		
		System.out.println("+ Selecting ! +");
		app.select();
		try{
			System.out.println("+ Processing ! +");
			app.process(apdu);
			apdu.JCRE_return();
		}catch(ISOException e){
			System.err.println(e.getReason());
			apdu.JCRE_return_Exception(e.getReason());
		}finally{
			System.out.println("+ Deselecting ! +");
			app.deselect();
		}
	}
		

}
		     */
		    
		    writer.close();
		    l.w(Logger.INFO, "Le fichier Main.java est bien créé !");
		    
		    return true;
		} catch (IOException e) {
			l.w(Logger.ERR, "Impossible de créer un fichier !");
		   return false;
		}
	}

}
