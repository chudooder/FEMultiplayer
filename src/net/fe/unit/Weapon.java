package net.fe.unit;

import java.util.*;

import net.fe.fightStage.Brave;
import net.fe.fightStage.CombatTrigger;

public class Weapon extends Item{
	public HashMap<String, Integer> modifiers;
	public int mt, hit, crit;
	public List<Integer> range;
	public Type type;
	public ArrayList<String> effective;
	public int worth;
	public String pref;

	
	public Weapon(String name) {
		super(name);
		// Initialize modifiers to 0
		modifiers = new HashMap<String, Integer>();
		modifiers.put("Skl", 0);
		modifiers.put("Lck", 0);
		modifiers.put("HP",  0);
		modifiers.put("Str", 0);
		modifiers.put("Mag", 0);
		modifiers.put("Def", 0);
		modifiers.put("Res", 0);
		modifiers.put("Spd", 0);
		modifiers.put("Lvl", 0);
		modifiers.put("Mov", 0);
		mt = 0;
		hit = 0;
		crit = 0;
		type = null;
		effective = new ArrayList<String>();
	}
	
	public enum Type{
		SWORD, LANCE, AXE, BOW, LIGHT, ANIMA, DARK, STAFF;
		public int triangleModifier(Type other){
			switch(this){
			case SWORD:
				if(other == LANCE) return -1;
				if(other == AXE) return 1;
				return 0;
			case LANCE:
				if(other == AXE) return -1;
				if(other == SWORD) return 1;
				return 0;
			case AXE:
				if(other == SWORD) return -1;
				if(other == LANCE) return 1;
				return 0;
				
			case LIGHT:
				if(other == ANIMA) return -1;
				if(other == DARK) return 1;
				return 0;
			case ANIMA:
				if(other == DARK) return -1;
				if(other == LIGHT) return 1;
				return 0;
			case DARK:
				if(other == LIGHT) return -1;
				if(other == ANIMA) return 1;
				return 0;
			default:
				return 0;
			}
		}
		
		public boolean isMagic(){
			return this == ANIMA || this == LIGHT || this == DARK;
		}
	}
	//Returns 1 if advantage, -1 if disadvantage
	public int triMod(Weapon other){ 
		if(other == null) return 0;
		if(this.name.contains("reaver") || other.name.contains("reaver")){
			if(this.name.contains("reaver") && other.name.contains("reaver")){
				return -type.triangleModifier(other.type);
			}
			return -2*type.triangleModifier(other.type);
		}
		return type.triangleModifier(other.type);
	}
	
	public boolean isMagic(){
		return type.isMagic();
	}
	
	public List<CombatTrigger> getTriggers(){
		ArrayList<CombatTrigger> triggers = new ArrayList<CombatTrigger>();
		if(name.contains("Brave")){
			triggers.add(new Brave());
		}
		return triggers;
	}
	
	
	public Weapon getCopy(){
		Weapon w = new Weapon(name);
		w.type = type;
		w.range = new ArrayList<Integer>(range);
		w.mt = mt;
		w.hit = hit;
		w.crit = crit;
		w.setMaxUses(getMaxUses());
		w.worth = worth;
		w.effective = new ArrayList<String>(effective);
		w.pref = pref;
		w.modifiers = new HashMap<String, Integer>(modifiers);
		w.id = id;
		return w;
		
	}
}
