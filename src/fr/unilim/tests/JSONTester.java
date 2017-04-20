package fr.unilim.tests;

import java.nio.file.Paths;

import fr.unilim.JSON.JSONFilter;
import fr.unilim.utils.FileUtil;

public class JSONTester {

	public static void main(String[] args) {
		JSONFilter jf = new JSONFilter(FileUtil.fileToString(Paths.get("test/resources/test_arrays_m4.json").toFile()));
		System.out.println(jf.filterByDecision("2"));
		
		System.out.println("----------------");
		
		System.out.println(jf.filterByResult("ERROR"));
	}

}
