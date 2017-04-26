package fr.unilim.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import fr.unilim.jacacoco.tests.model.AppletModel;
import fr.unilim.jacacoco.tests.model.TestModel;
import fr.unilim.tests.exception.APDUValuesReaderException;

public class APDUValuesReaderTester {


	public static void main(String[] args) {
		File f = new File("test/resources/values.txt");
		int nbBuffer = 0;
		AppletModel appModel = new AppletModel();
		List<TestModel> tests = new ArrayList<>();
		TestModel t;
		Byte[] data;
		int lc;
		try(APDUValuesReader apdureader = new APDUValuesReader(new BufferedReader(new FileReader(f)))){
			byte[] buffer;
			while((buffer = apdureader.nextBuffer()) != null){
				for(int i = 0; i < buffer.length; ++i){
					System.out.print(buffer[i] + " ");
				}
				System.out.println(" lenght : " + buffer.length);
				nbBuffer++;
				t = new TestModel();
				t.setCla(buffer[0]);
				t.setIns(buffer[1]);
				t.setP1(buffer[2]);
				t.setP2(buffer[3]);
				t.setName("Test-" + nbBuffer);
				lc = buffer[4];
				if(lc > 0){
					data = new Byte[lc];
					for(byte i = 0; i < lc; ++i){
						if(5 + i < buffer.length){
							data[i] = buffer[5 + i];
						}else{
							data[i] = 0;
						}
					}
					t.setData(data);
				}else{
					lc = 0;
				}
				
				if(5 + lc < buffer.length){
					t.setLenght(buffer[(byte)(5 + lc)]);					
				}
				System.out.println(t);
				tests.add(t);
			}
			System.out.println("Read " + nbBuffer + " buffers");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (APDUValuesReaderException e) {
			e.printStackTrace();
			return;
		}
		
		appModel.setTests(tests);
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(AppletModel.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			marshaller.marshal(appModel, new FileOutputStream("tests_jadart.xml"));
		} catch (JAXBException | FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
