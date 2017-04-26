package fr.unilim.application.gui.tasks;

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

import fr.unilim.jacacoco.application.gui.concurrent.TaskException;
import fr.unilim.jacacoco.tests.model.AppletModel;
import fr.unilim.tests.APDUValuesReader;
import fr.unilim.tests.BuffersToJacacocoTests;
import fr.unilim.tests.exception.APDUValuesReaderException;
import javafx.concurrent.Task;

public class GenerateJCCCTestsTask extends Task<Void>{

	private File valuesFile;
	private File destFile;
	
	@Override
	protected Void call() throws Exception {
		updateMessage("Generating ...");
		AppletModel appModel = new AppletModel();
		List<byte[]> tests = new ArrayList<>();
		try(APDUValuesReader apdureader = new APDUValuesReader(new BufferedReader(new FileReader(valuesFile)))){
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
			throw new TaskException(valuesFile + " : file not found.", e);
		} catch (IOException e) {
			throw new TaskException(valuesFile + " : file can't open..", e);
		} catch (APDUValuesReaderException e) {
			throw new TaskException(valuesFile + " : malformed file.", e);
		}
		
		appModel = BuffersToJacacocoTests.convert(tests);
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(AppletModel.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			marshaller.marshal(appModel, new FileOutputStream(destFile));
		} catch (JAXBException | FileNotFoundException e) {
			throw new TaskException(destFile + " can't write", e);
		}
		updateMessage("Done");
		return null;
	}

	public File getValuesFile() {
		return valuesFile;
	}

	public void setValuesFile(File valuesFile) {
		this.valuesFile = valuesFile;
	}

	public File getDestFile() {
		return destFile;
	}

	public void setDestFile(File destFile) {
		this.destFile = destFile;
	}
}
