package fr.unilim.tests;

import java.nio.file.Paths;

import fr.unilim.Logger;
import fr.unilim.Master;

public class MainTester {

	public static void main(String[] args) {
		Master master = new Master(Paths.get("/home/simon/monTest"), Paths.get("/home/simon/montestgen"), "APDU", "com.simsor.apdu");
		master.generate();
		
		System.out.println("Generated successfully");
	}

}
