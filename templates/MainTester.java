 package fr.unilim.javelin;

import ${packageName}.${appletClsName};
import javacard.framework.ISOException;
import javacard.framework.APDU;
// import lhs.simu.jc.JCRE;

public class MainTester {

	public static void main(String[] args) {
		System.out.println("+ In main! +");

		
		// new JCRE();	//we init the run env for JavaCard
		
		APDU mAPDU = new APDU();
		
		//byte[] theBuffer = {MyFutureApplet.CLA_TESTAPPLET, MyFutureApplet.INS_INTERROGER_COMPTEUR, (byte) 0, (byte) 0, (byte) 5};
		//run(new MyFutureApplet(), mAPDU);
		
		run(new ${appletClsName}(), mAPDU);


	}
	
	public static void run(${appletClsName} app, APDU apdu){
		
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