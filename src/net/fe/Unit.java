package net.fe;

import java.util.ArrayList;
import java.util.HashMap;

import chu.engine.Entity;

public class Unit extends Entity {
	private HashMap<String, Float> stats;
	private int hp;
	private Clazz clazz;
	private HashMap<String, Integer> growths;
	private Weapon weapon;
	private ArrayList<Item> inventory;
	private HashMap<String, Integer> tempMods;

	public Unit(float x, float y, HashMap<String, Float> startingStats,
			HashMap<String, Integer> growths) {
		super(x, y);
		stats = startingStats;
		this.growths = growths;
	}
	
	@Override
	public void beginStep() {
		// TODO Auto-generated method stub
	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub

	}
	
	public void levelUp(){
		stats.put("Lvl", stats.get("Lvl") + 1);
		for(String stat: growths.keySet()){
			stats.put(stat, stats.get(stat) + (float)(growths.get(stat)/100.0));
		}
	}
	
	public void equip(int index){
		weapon = (Weapon) inventory.get(index);
	}
	
	//TODO: getTriggers

	
	//Combat statistics
	public int hit(){
		return weapon.hit + get("Skl") + get("Luk")/2 +
				(tempMods.get("Hit")!=null?tempMods.get("Hit"):0);
	}
	
	public int avoid(){
		return get("Spd") + get("Luk")/2 +
				(tempMods.get("Avo")!=null?tempMods.get("Avo"):0);
		//TODO: terrain bonus
	}
	
	public int crit(){
		return weapon.crit + get("Skl")/2 + clazz.crit +
				(tempMods.get("Crit")!=null?tempMods.get("Crit"):0);
	}
	
	public int dodge(){ //Critical avoid
		return get("Luk")+
				(tempMods.get("Dodge")!=null?tempMods.get("Dodge"):0);
	}

	
	//Getter/Setter
	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public int get(String stat){
		return stats.get(stat).intValue() + weapon.modifiers.get(stat) +
				(tempMods.get(stat)!=null?tempMods.get(stat):0);
	}
	
	public Weapon getWeapon(){
		return weapon;
	}

}
