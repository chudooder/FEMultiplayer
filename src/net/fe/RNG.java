package net.fe;

import java.util.Random;

public class RNG {
	
	private static Random RNG = new Random();
	public static void seed(long rng){
		RNG.setSeed(rng);
	}
	public static int get(){
		return RNG.nextInt(100);
	}
	
	static{
		RNG = new Random();
		long seed = RNG.nextLong();
		RNG.setSeed(seed);
		System.out.println("Seed:" + seed);
	}
}
