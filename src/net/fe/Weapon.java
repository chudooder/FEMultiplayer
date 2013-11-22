package net.fe;

import java.util.*;

public class Weapon extends Item{
	public HashMap<String, Integer> modifiers;
	public int mt, hit, crit;
	public int[] range;
	public Type type;
	public ArrayList<Clazz> effective;
	
	public Weapon() {
		// Initialize modifiers to 0
		modifiers = new HashMap<String, Integer>();
		modifiers.put("Skl", 0);
		modifiers.put("Luk", 0);
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
		effective = new ArrayList<Clazz>();
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
		return type.triangleModifier(other.type);
	}
	
	public boolean isMagic(){
		return type.isMagic();
	}
	//TODO: Trigger
	
	public static Weapon createWeapon(String name) {
		Weapon weapon = new Weapon();
		if(name.equals("sord")) {
			weapon.type = Weapon.Type.SWORD;
			weapon.mt = 3;
			weapon.hit = 70;
			weapon.crit = 30;
			weapon.range = new int[]{1};
			return weapon;
		}
		return null;
	}
}
