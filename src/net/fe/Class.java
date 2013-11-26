package net.fe;

import java.util.Arrays;
import java.util.List;

import net.fe.Weapon.Type;
import net.fe.trigger.*;

public class Class {
	public final int crit;
	public final Trigger masterSkill;
	public final List<Type> usableWeapon;
	public final String name;
	private Class(String name, int c, Trigger m, Weapon.Type... types){
		crit = c;
		masterSkill = m;
		usableWeapon = Arrays.asList(types);
		this.name = name;
	}
	public static Class createClass(String name){
		if(name == null)
			return new Class("Test", 0, null, Weapon.Type.SWORD, Weapon.Type.LANCE);
		if(name.equals("Sniper"))
			return new Class("Sniper",10, new Deadeye(), Weapon.Type.BOW);
		if(name.equals("Hero"))
			return new Class("Hero",0, new Colossus(), Weapon.Type.SWORD, Weapon.Type.AXE);
		if(name.equals("Berserker"))
			return new Class("Berserker", 10, new Luna(), Weapon.Type.AXE);
		if(name.equals("Assassin"))
			return new Class("Assassin", 10, new Lethality(), Weapon.Type.BOW, Weapon.Type.SWORD);
		if(name.equals("Paladin"))
			return new Class("Paladin", 0, new Sol(), Weapon.Type.LANCE, Weapon.Type.SWORD);
		return null;
		
	}
}
