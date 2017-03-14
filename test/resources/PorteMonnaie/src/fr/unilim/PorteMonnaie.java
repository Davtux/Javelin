package fr.unilim;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.Util;

public class PorteMonnaie extends Applet {

	private short balance;
	
	public PorteMonnaie() {
		register();
	}

	public static void install(byte bArray[], short bOffset, byte bLength) throws ISOException {
		new PorteMonnaie();
	}

	public void process(APDU apdu) throws ISOException {
		byte[] buffer = apdu.getBuffer();
		
		if(selectingApplet()){ return; }
		
		if(buffer[ISO7816.OFFSET_CLA] != (byte) 0x80) 
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		
		short octetsLus;
		
		switch(buffer[ISO7816.OFFSET_INS]){
		case (byte) 0x34:
			octetsLus = apdu.setIncomingAndReceive();
			if(octetsLus != (short) 0)
				ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
			if(buffer[ISO7816.OFFSET_P1] != (byte) 0x00 && buffer[ISO7816.OFFSET_P2] != (byte) 0x00 )
				ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
			Util.setShort(buffer, (short) 0, balance);
			apdu.setOutgoingAndSend((short)0 , (short)2);
			return;
		case (byte) 0x36:
			octetsLus = apdu.setIncomingAndReceive();
			if(octetsLus != (short) 2)
				ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
			short amont = Util.getShort(buffer, (short)5);
			if(buffer[ISO7816.OFFSET_P1] != (byte) 0x00 && buffer[ISO7816.OFFSET_P2] != (byte) 0x00 )
				ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
			balance += amont;
			return;
		case (byte) 0x38:
			octetsLus = apdu.setIncomingAndReceive();
			if(octetsLus != (short)2)
				ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
			short delMont = Util.getShort(buffer, (short)5);
			if(buffer[ISO7816.OFFSET_P1] != (byte)0x00 && buffer[ISO7816.OFFSET_P2] != (byte)0x00)
				ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
			if(balance < delMont)
				ISOException.throwIt(ISO7816.SW_COMMAND_NOT_ALLOWED);
			balance -= delMont;
			return;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
