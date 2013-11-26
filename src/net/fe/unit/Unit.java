package net.fe.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import chu.engine.GriddedEntity;
import net.fe.Trigger;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.Terrain;


public class Unit extends GriddedEntity {
	private HashMap<String, Float> stats;
	private int hp;
	private Class clazz;
	private HashMap<String, Integer> growths;
	private Weapon weapon;
	private ArrayList<Item> inventory;
	private HashMap<String, Integer> tempMods;
	public final String name;
	//TODO Rescue

	public Unit(String name, Class c, HashMap<String, Float> startingStats,
			HashMap<String, Integer> growths) {
		super(0, 0);
		stats = startingStats;
		hp = (int)(startingStats.get("HP").floatValue());
		this.growths = growths;
		inventory = new ArrayList<Item>();
		tempMods = new HashMap<String, Integer>();
		this.name = name;
		clazz = c;
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
		if(stats.get("Lvl") == 20){
			return;
		}
		stats.put("Lvl", stats.get("Lvl") + 1);
		for(String stat: growths.keySet()){
			int hpOld = get("HP");
			stats.put(stat, stats.get(stat) + (float)(growths.get(stat)/100.0));
			hp += get("HP") - hpOld;
		}
	}
	
	public void equip(int index){
		if(equippable(index))
			weapon = (Weapon) inventory.get(index);
		else
			throw new IllegalArgumentException("Cannot equip that item");
			
	}
	
	public boolean equippable(int index){
		if(inventory.get(index) instanceof Weapon){
			return clazz.usableWeapon.contains(((Weapon) inventory.get(index)).type);
		}
		return false;
	}
	
	public void clearTempMods(){
		tempMods.clear();
	}
	
	public ArrayList<Trigger> getTriggers(){
		ArrayList<Trigger> triggers = new ArrayList<Trigger>();
		if(clazz.masterSkill!=null)
			triggers.add(clazz.masterSkill);
		return triggers;
	}

	
	//Combat statistics
	public int hit(){
		return weapon.hit + get("Skl") + get("Lck")/2 +
				(tempMods.get("Hit")!=null?tempMods.get("Hit"):0);
	}
	
	public int avoid(){
		return get("Spd") + get("Lck")/2 +
				(tempMods.get("Avo")!=null?tempMods.get("Avo"):0) +
				getTerrain().avoidBonus;
	}
	
	public int crit(){
		return weapon.crit + get("Skl")/2 + clazz.crit +
				(tempMods.get("Crit")!=null?tempMods.get("Crit"):0);
	}
	
	public int dodge(){ //Critical avoid
		return get("Lck")+
				(tempMods.get("Dodge")!=null?tempMods.get("Dodge"):0);
	}

	
	//Getter/Setter
	public Class getTheClass() {
		return clazz;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = Math.max(hp,0);
	}
	
	public int get(String stat){
		int ans = stats.get(stat).intValue() + (weapon!=null?weapon.modifiers.get(stat):0) +
				(tempMods.get(stat)!=null?tempMods.get(stat):0);
		if(Arrays.asList("Def","Res").contains(stat)){
			ans += getTerrain().defenseBonus;
		}
		return ans;
	}
	
	public int getBase(String stat){
		return stats.get(stat).intValue();
	}
	
	public void setTempMod(String stat, int val){
		tempMods.put(stat, val);
	}
	
	public Weapon getWeapon(){
		return weapon;
	}
	
	public void addToInventory(Item item) {
		inventory.add(item);
	}
	
	public Terrain getTerrain(){
		return ((OverworldStage) stage).getTerrain(xcoord, ycoord);
	}
	
	//Debugging
	public String toString(){
		return name + " HP" + hp + "\n" + stats;
	}
}
