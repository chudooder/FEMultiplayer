package net.fe.unit;

import java.util.*;

public class Weapon extends Item{
	public HashMap<String, Integer> modifiers;
	public int mt, hit, crit;
	public List<Integer> range;
	public Type type;
	public ArrayList<Class> effective;
	
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
		effective = new ArrayList<Class>();
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
		return type.triangleModifier(other.type);
	}
	
	public boolean isMagic(){
		return type.isMagic();
	}
	//TODO: Trigger
	
	public static Weapon createWeapon(String name) {
		Weapon weapon = new Weapon(name);
		if(name.equals("sord")) {
			weapon.type = Weapon.Type.SWORD;
			weapon.mt = 3;
			weapon.hit = 90;
			weapon.crit = 30;
			weapon.range = Arrays.asList(1);
			return weapon;
		}
		
		if(name.equals("lunce")) {
			weapon.type = Weapon.Type.LANCE;
			weapon.mt = 4;
			weapon.hit = 80;
			weapon.crit = 10;
			weapon.range = Arrays.asList(1);
			return weapon;
		}
		
		if(name.equals("bow")){
			weapon.type = Weapon.Type.BOW;
			weapon.mt = 4;
			weapon.hit = 100;
			weapon.crit = 0;
			weapon.range = Arrays.asList(2);
			return weapon;
		}
		
		if(name.equals("axe")){
			weapon.type = Weapon.Type.AXE;
			weapon.mt = 6;
			weapon.hit = 75;
			weapon.crit = 10;
			weapon.range = Arrays.asList(1);
			return weapon;
		}
		return null;
	}
}
