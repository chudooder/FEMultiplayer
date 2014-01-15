package net.fe.fightStage.anim;

import net.fe.unit.Unit;
import net.fe.unit.Weapon;
import net.fe.unit.Weapon.Type;
import net.fe.unit.WeaponFactory;

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
		Weapon w = u.getWeapon();
		if(w == null){
			Type wType = u.getTheClass().usableWeapon.get(0);
			if(wType.isMagic()){
				wepAnimName = "magic";
				classification = "magic";
			} else {
				wepAnimName = wType.toString().toLowerCase();
				classification = "normal";
			}
		}else if(w.isMagic()){
			wepAnimName = "magic";
			classification = "magic";
		} else {
			wepAnimName = w.type.toString().toLowerCase();
			classification = "normal";
			if(w.range.contains(range) && range > 1){
				if(w.type == Weapon.Type.AXE){
					wepAnimName = "handaxe";
					classification = "ranged";
				}
				if (w.type == Weapon.Type.LANCE){
					wepAnimName = "javelin";
					classification = "ranged";
				} 
				if (w.type == Weapon.Type.SWORD){
					wepAnimName = "rangedsword";
					classification = "ranged";
				}
			}
			if (w.type == Weapon.Type.BOW){
				this.classification = "ranged";
			}
		}
	}
}
