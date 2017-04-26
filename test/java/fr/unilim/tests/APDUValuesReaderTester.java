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
		AppletModel appModel = new AppletModel();
		List<byte[]> tests = new ArrayList<>();
		try(APDUValuesReader apdureader = new APDUValuesReader(new BufferedReader(new FileReader(f)))){
			byte[] buffer;
			while((buffer = apdureader.nextBuffer()) != null){
				for(int i = 0; i < buffer.length; ++i){
					System.out.print(buffer[i] + " ");
				}
				System.out.println();
				tests.add(buffer);
			}
			System.out.println("Read " + tests.size() + " buffers");
			
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
		
		appModel = BuffersToJacacocoTests.convert(tests);
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(AppletModel.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			marshaller.marshal(appModel, new FileOutputStream("test/resources/tests_jdart.xml"));
		} catch (JAXBException | FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
