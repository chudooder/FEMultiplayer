package net.fe;

import java.util.Arrays;
import java.util.List;

import net.fe.Weapon.Type;

public class Class {
	public final int crit;
	public final Trigger masterSkill;
	public final List<Type> usableWeapon;
	private Class(int c, Trigger m, Weapon.Type... types){
		crit = c;
		masterSkill = m;
		usableWeapon = Arrays.asList(types);
	}
	public static Class createClass(String name){
		return new Class(0, null, Weapon.Type.SWORD, Weapon.Type.LANCE);
	}
}
