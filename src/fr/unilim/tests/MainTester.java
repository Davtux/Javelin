package fr.unilim.tests;

import java.nio.file.Paths;

import fr.unilim.Master;

public class MainTester {

	public static void main(String[] args) {
		Master master = new Master(Paths.get("/home/marwen/monTest"), Paths.get("/home/marwen/montestgen"), "APDU", "com.zeruf.apdu");
		master.execute(Paths.get("/home/marwen/inria/jDart-proj/jpf-core/bin/jpf"), Paths.get("/home/marwen/inria/jDart-proj/z3/build"));
		
		
		//SUTIntegrator suti = new SUTIntegrator(Paths.get("/home/marwen/test_dest"));
		//suti.integrate(Paths.get("/home/marwen/test1"));
		System.out.println("Generated successfully");
	}

}
