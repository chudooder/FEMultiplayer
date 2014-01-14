package net.fe.unit;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import net.fe.fightStage.*;

public class Class implements Serializable {
	private static final long serialVersionUID = 9144407404798873761L;
	public final int crit;
	public final CombatTrigger masterSkill;
	public final List<Weapon.Type> usableWeapon;
	public final String name;
	private Class(String name, int c, CombatTrigger m, Weapon.Type... types){
		crit = c;
		masterSkill = m;
		usableWeapon = Arrays.asList(types);
		this.name = name;
	}
	
	public static Class createClass(String name){
		//Lords
		if(name.equals("Roy"))
			return new Class("Lord", 0, new Aether(), Weapon.Type.SWORD);
		if(name.equals("Eliwood"))
			return new Class("Lord", 0, new Sol(false), Weapon.Type.SWORD, Weapon.Type.LANCE);
		if(name.equals("Lyn"))
			return new Class("Lord", 0, new Astra(), Weapon.Type.SWORD, Weapon.Type.BOW);
		if(name.equals("Hector"))
			return new Class("Lord", 0, new Luna(false), Weapon.Type.AXE, Weapon.Type.SWORD);
		if(name.equals("Eirika"))
			return new Class("Lord", 0, new Luna(false), Weapon.Type.SWORD);
		if(name.equals("Ephraim"))
			return new Class("Lord", 0, new Sol(false), Weapon.Type.LANCE);
		if(name.equals("Marth"))
			return new Class("Lord", 0, new Aether(), Weapon.Type.SWORD);
		if(name.equals("Ike"))
			return new Class("Lord", 0, new Aether(), Weapon.Type.SWORD, Weapon.Type.AXE);
		
		//Other
		if(name.equals("Sniper"))
			return new Class("Sniper",10, new Deadeye(), Weapon.Type.BOW);
		if(name.equals("Hero"))
			return new Class("Hero",0, new Colossus(), Weapon.Type.SWORD, Weapon.Type.AXE);
		if(name.equals("Berserker"))
			return new Class("Berserker", 10, new Luna(false), Weapon.Type.AXE);
		if(name.equals("Warrior"))
			return new Class("Warrior", 0, new Colossus(), Weapon.Type.AXE, Weapon.Type.BOW);
		if(name.equals("Assassin"))
			return new Class("Assassin", 10, new Lethality(), Weapon.Type.SWORD);
		if(name.equals("Paladin"))
			return new Class("Paladin", 0, new Sol(false), Weapon.Type.LANCE, Weapon.Type.SWORD);
		if(name.equals("Sage"))
			return new Class("Sage", 0, new Sol(true), Weapon.Type.ANIMA, Weapon.Type.STAFF);
		if(name.equals("General"))
			return new Class("General", 0, new Pavise(), Weapon.Type.AXE, Weapon.Type.LANCE);
		if(name.equals("Valkyrie"))
			return new Class("Valkyrie", 0, new Miracle(), Weapon.Type.STAFF, Weapon.Type.LIGHT);
		if(name.equals("Swordmaster"))
			return new Class("Swordmaster", 20, new Astra(), Weapon.Type.SWORD);
		if(name.equals("Sorcerer"))
			return new Class("Sorcerer", 0, new Luna(true), Weapon.Type.DARK, Weapon.Type.ANIMA);
		if(name.equals("Falconknight"))
			return new Class("Falconknight", 0, new Crisis(), Weapon.Type.LANCE, Weapon.Type.SWORD);
		return null;
		
	}
}
