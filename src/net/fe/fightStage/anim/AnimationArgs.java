package net.fe.fightStage.anim;

import net.fe.unit.Unit;
import net.fe.unit.Weapon;

public class AnimationArgs {
	public String userclass;
	public String wepAnimName;
	public String classification;
	public boolean left;
	public Unit unit;
	public int range;
	
	public AnimationArgs(Unit u, boolean left, int range){
		if(u.getTheClass().name.equals("Lord")){
			userclass = u.name;
		} else {
			userclass = u.getTheClass().name;
		}
		this.left = left;
		this.range = range;
		unit = u;
		if(u.getWeapon().isMagic()){
			wepAnimName = "magic";
			classification = "magic";
		} else {
			wepAnimName = u.getWeapon().type.toString().toLowerCase();
			classification = "normal";
			if(u.getWeapon().range.contains(range) && range > 1){
				if(u.getWeapon().type == Weapon.Type.AXE){
					wepAnimName = "handaxe";
					classification = "ranged";
				}
				if (u.getWeapon().type == Weapon.Type.LANCE){
					wepAnimName = "javelin";
					classification = "ranged";
				} 
			}
			if (u.getWeapon().type == Weapon.Type.BOW){
				this.classification = "ranged";
			}
		}
	}
}
