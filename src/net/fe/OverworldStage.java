package net.fe;

import java.util.HashMap;

public class OverworldStage {
	private Grid grid;
	
	public OverworldStage() {
		super();
		grid = new Grid(10, 10);
		// TODO: Beta testing stuff, delete later
		HashMap<String, Float> stats1 = new HashMap<String, Float>();
		stats1.put("Skl", 10f);
		stats1.put("Lck", 1f);
		stats1.put("HP", 15f);
		stats1.put("Str", 10f);
		stats1.put("Mag", 10f);
		stats1.put("Def", 10f);
		stats1.put("Res", 10f);
		stats1.put("Spd", 12f);
		stats1.put("Lvl", 1f);
		stats1.put("Mov", 3f);
		
		HashMap<String, Integer> growths1 = new HashMap<String, Integer>();
		growths1.put("HP", 70);
		growths1.put("Str", 50);
		growths1.put("Mag", 10);
		growths1.put("Skl", 70);
		growths1.put("Spd", 70);
		growths1.put("Def", 40);
		growths1.put("Res", 30);
		growths1.put("Lck", 60);
		
		HashMap<String, Float> stats2 = new HashMap<String, Float>();
		stats2.put("Skl", 10f);
		stats2.put("Lck", 3f);
		stats2.put("HP", 15f);
		stats2.put("Str", 10f);
		stats2.put("Mag", 10f);
		stats2.put("Def", 10f);
		stats2.put("Res", 10f);
		stats2.put("Spd", 8f);
		stats2.put("Lvl", 1f);
		stats2.put("Mov", 3f);
		
		HashMap<String, Integer> growths2 = new HashMap<String, Integer>();
		growths2.put("HP", 70);
		growths2.put("Str", 60);
		growths2.put("Mag", 10);
		growths2.put("Skl", 60);
		growths2.put("Spd", 50);
		growths2.put("Def", 40);
		growths2.put("Res", 30);
		growths2.put("Lck", 60);
		
		Unit left = new Unit("Marth",Class.createClass("Assassin"), stats1, growths1);
		left.addToInventory(Weapon.createWeapon("sord"));
		left.equip(0);
		
		Unit right = new Unit("Roy",Class.createClass(null), stats2, growths2);
		right.addToInventory(Weapon.createWeapon("lunce"));
		right.equip(0);
		
		for(int i = 0; i < 15; i++){
			left.levelUp();
			right.levelUp();
		}
		
		grid.addUnit(left, 0, 0);
		grid.addUnit(right, 1, 0);
	}
}
