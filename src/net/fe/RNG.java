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
		long seed = RNG.nextLong();
		RNG.setSeed(seed);
		// Temporarily rig the RNG
//		System.out.println("**********************************");
//		System.out.println("******WARNING: RNG IS RIGGED******");
//		System.out.println("**********************************");
//		RNG.setSeed(3171284040465844943L);
		System.out.println("Seed:" + seed);
	}
}
