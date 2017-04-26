package fr.unilim.tests;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.counter.Counter;
import fr.unilim.jacacoco.tests.model.AppletModel;
import fr.unilim.jacacoco.tests.model.TestModel;

/**
 * Converts bytes to {@link fr.unilim.jacacoco.tests.model.AppletModel}
 */
public class BuffersToJacacocoTests {
	
	private final static Logger log = LoggerFactory.getLogger(BuffersToJacacocoTests.class);

	private BuffersToJacacocoTests(){}
	
	public static AppletModel convert(List<byte[]> buffers){
		TestModel test;
		AppletModel applet = new AppletModel();
		List<TestModel> tests = new ArrayList<>();
		Counter counterTest = new Counter();
		short lc;
		Byte[] data;
		for(byte[] buffer : buffers){
			test = new TestModel();
			test.setCla(buffer[0]);
			test.setIns(buffer[1]);
			test.setP1(buffer[2]);
			test.setP2(buffer[3]);
			test.setName("Test-" + counterTest.nextString());
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
				test.setData(data);
			}else{
				lc = 0;
			}
			
			if(5 + lc < buffer.length){
				test.setLenght(buffer[(byte)(5 + lc)]);					
			}
			
			log.info("Create test : {}", test);
			tests.add(test);
		}
		applet.setTests(tests);
		return applet;
	}
}
