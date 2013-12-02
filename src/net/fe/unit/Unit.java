package net.fe.unit;

import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import chu.engine.GriddedEntity;
import chu.engine.Resources;
import net.fe.Party;
import net.fe.fightStage.*;
import net.fe.fightStage.anim.AnimationArgs;
import net.fe.fightStage.anim.AttackAnimation;
import net.fe.fightStage.anim.DodgeAnimation;
import net.fe.fightStage.anim.NormalAttack;
import net.fe.overworldStage.*;



public class Unit extends GriddedEntity {
	private HashMap<String, Float> stats;
	private HashMap<String, Integer> bases;
	private int hp;
	private Class clazz;
	private HashMap<String, Integer> growths;
	private Weapon weapon;
	private ArrayList<Item> inventory;
	private HashMap<String, Integer> tempMods;
	public final String name;
	private Party team;
	//TODO Rescue

	public Unit(String name, Class c, HashMap<String, Integer> bases,
			HashMap<String, Integer> growths) {
		super(0, 0);
		this.bases = bases;
		this.growths = growths;
		inventory = new ArrayList<Item>();
		tempMods = new HashMap<String, Integer>();
		this.name = name;
		clazz = c;
		
		stats = new HashMap<String, Float>();
		for(String s: bases.keySet()){
			stats.put(s, bases.get(s).floatValue());
		}
			
		fillHp();
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
		if(stats.get("Lvl") == 40){
			return;
		}
		stats.put("Lvl", stats.get("Lvl") + 1);
		for(String stat: growths.keySet()){
			stats.put(stat, stats.get(stat) + (float)(growths.get(stat)/100.0));
		}
	}
	
	public void setLevel(int lv){
		stats.put("Lvl", (float) lv);
		lv--;
		for(String stat: growths.keySet()){
			stats.put(stat, bases.get(stat) + (float)(lv*growths.get(stat)/100.0));
		}
	}
	
	public void fillHp(){
		setHp(get("HP"));
	}
	
	public void equip(int index){
		if(equippable(index))
			weapon = (Weapon) inventory.get(index);
		else
			throw new IllegalArgumentException("Cannot equip that item");
			
	}
	
	public boolean equippable(int index){
		if(inventory.get(index) instanceof Weapon){
			Weapon w = (Weapon) inventory.get(index);
			if(w.pref!=null){
				return name.equals(w.pref);
			}
			return clazz.usableWeapon.contains(w.type);
		}
		return false;
	}
	
	public ArrayList<CombatTrigger> getTriggers(){
		ArrayList<CombatTrigger> triggers = new ArrayList<CombatTrigger>();
		if(clazz.masterSkill!=null)
			triggers.add(clazz.masterSkill);
		triggers.addAll(weapon.getTriggers());
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
	
	public void clearTempMods(){
		tempMods.clear();
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
	
	public Color getPartyColor(){
		return team.getColor();
	}
	
	public void setParty(Party t){
		team = t;
	}
	
	public Party getParty(){
		return team;
	}
}
