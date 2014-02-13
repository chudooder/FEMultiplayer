package net.fe;

import java.util.Random;

public class RNG {
	public static long seed;
	private static Random RNG = new Random();
	public static void seed(long rng){
		RNG.setSeed(rng);
	}
	public static int get(){
		return RNG.nextInt(100);
	}
	
	static{
		RNG = new Random();
		seed = RNG.nextLong();
		RNG.setSeed(4820426393665352053L);
		// Temporarily rig the RNG
//		System.out.println("**********************************");
//		System.out.println("******WARNING: RNG IS RIGGED******");
//		System.out.println("**********************************");
//		RNG.setSeed(-3609682453736580738l);
//		System.out.println("Seed:" + seed);
	}
}
