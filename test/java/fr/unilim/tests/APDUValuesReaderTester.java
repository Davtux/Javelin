package fr.unilim.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import fr.unilim.tests.exception.APDUValuesReaderException;

public class APDUValuesReaderTester {

	public static void main(String[] args) {
		File f = new File("test/resources/values.txt");
		int nbBuffer = 0;
		try(APDUValuesReader apdureader = new APDUValuesReader(new BufferedReader(new FileReader(f)))){
			byte[] buffer;
			while((buffer = apdureader.nextBuffer()) != null){
				for(int i = 0; i < buffer.length; ++i){
					System.out.print(buffer[i] + "\t");
				}
				System.out.println(" lenght : " + buffer.length);
				nbBuffer++;
			}
			System.out.println("Read " + nbBuffer + " buffers");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (APDUValuesReaderException e) {
			e.printStackTrace();
		}
	}

}
