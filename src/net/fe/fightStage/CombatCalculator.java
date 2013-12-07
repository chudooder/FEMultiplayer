package net.fe.fightStage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import net.fe.FEMultiplayer;
import net.fe.RNG;
import net.fe.overworldStage.Grid;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Weapon;

public class CombatCalculator {
	private Unit left, right;
	private ArrayList<AttackRecord> attackQueue;
	private int range;
	public CombatCalculator(UnitIdentifier u1, UnitIdentifier u2){
		left = FEMultiplayer.getUnit(u1);
		right = FEMultiplayer.getUnit(u2);
		range = Grid.getDistance(left, right);
		attackQueue = new ArrayList<AttackRecord>();
		calculate();
	}
	private void calculate() {
		int hpLeft = left.getHp();
		int hpRight = right.getHp();
		// Determine turn order
		ArrayList<Boolean> attackOrder = new ArrayList<Boolean>();
		if (shouldAttack(left,right,range,true))
			attackOrder.add(true);
		if (shouldAttack(right,left,range,false))
			attackOrder.add(false);
		if (left.get("Spd") >= right.get("Spd") + 4 
				&& shouldAttack(left,right,range,false)) {
			attackOrder.add(true);
		}
		if (right.get("Spd") >= left.get("Spd") + 4
				&& shouldAttack(right,left,range,false)) {
			attackOrder.add(false);
		}

		for (Boolean i : attackOrder) {
			attack(i, "None");
		}
		left.setHp(hpLeft);
		right.setHp(hpRight);
	}
	
	public static boolean shouldAttack(Unit a, Unit d, int range, boolean first){
		if(a.getWeapon() == null) return false;
		if(!a.getWeapon().range.contains(range)) return false;
		if(a.getWeapon().type == Weapon.Type.STAFF && !first) return false;
		if((a.getWeapon().type == Weapon.Type.STAFF)
				!= (a.getPartyColor().equals(d.getPartyColor()))) return false;
		return true;
	}

	public void attack(boolean leftAttacking, String currentEffect) {
		Unit a = leftAttacking?left: right;
		Unit d = leftAttacking?right: left;
		int damage = 0;
		int drain = 0;
		String animation = "Attack";
		boolean miss = false;
		int crit = 1;

		if (a.getHp() == 0 || d.getHp() == 0) {
			return;
		}

		LinkedHashMap<CombatTrigger, Boolean> aSuccess = 
				new LinkedHashMap<CombatTrigger, Boolean>();
		LinkedHashMap<CombatTrigger, Boolean> dSuccess = 
				new LinkedHashMap<CombatTrigger, Boolean>();
		
		for (CombatTrigger t : a.getTriggers()) {
			aSuccess.put(t,t.attempt(a));
		}
		for (CombatTrigger t : d.getTriggers()) {
			dSuccess.put(t,t.attempt(a));
		}
		
		

		for (CombatTrigger t : aSuccess.keySet()) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_PRE)!=0) {
				t.runPreAttack(this, a, d);
				if (t.nameModification == CombatTrigger.REPLACE_NAME_AFTER_PRE) {
					animation = t.getName() + "(a)";
				}
			}
		}	
		for (CombatTrigger t : dSuccess.keySet()) {
			if (dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_PRE)!=0) {
				t.runPreAttack(this, a, d);
				if (t.nameModification == CombatTrigger.REPLACE_NAME_AFTER_PRE) {
					animation = t.getName() + "(d)";
				}
			}
		}
		
		
		if (!((RNG.get()+RNG.get())/2 < a.hit() - d.avoid()
				+ a.getWeapon().triMod(d.getWeapon()) * 15)) {
			miss = true;
			if (a.getWeapon().isMagic())
				a.getWeapon().use(a);
		}
		if (RNG.get() < a.crit() - d.dodge()) {
			crit = 3;
			animation += " Critical(a)";
		}
		
		
		damage = calculateBaseDamage(a, d) * crit;
		for (CombatTrigger t : aSuccess.keySet()) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_MOD)!=0) {
				int oldDamage = damage;
				damage = t.runDamageMod(a,d,damage);
				if (t.nameModification == CombatTrigger.APPEND_NAME_AFTER_MOD 
						&& damage!=oldDamage) {
					animation += " " + t.getName() + "(a)";
				}
			}
		}
		for (CombatTrigger t : dSuccess.keySet()) {
			if (dSuccess.get(t)  && (t.turnToRun & CombatTrigger.ENEMY_TURN_MOD)!=0) {
				int oldDamage = damage;
				damage = t.runDamageMod(a,d,damage);
				if (t.nameModification == CombatTrigger.APPEND_NAME_AFTER_MOD 
						&& damage!=oldDamage) {
					animation += " " + t.getName() + "(d)";
				}
			}
		}
		damage = Math.max(0, Math.min(damage, d.getHp()));
		
		for (CombatTrigger t : aSuccess.keySet()) {
			if(aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_DRAIN)!=0){
				drain = t.runDrain(a, d, damage);
			}
		}
		for (CombatTrigger t : dSuccess.keySet()) {
			if(dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_DRAIN)!=0){
				drain = t.runDrain(a, d, damage);
			}
		}
		
		
		if(miss){
			damage = 0;
			animation = "Miss";
		}

		addToAttackQueue(a, d, animation, damage, drain);
		d.setHp(d.getHp() - damage);
		a.setHp(a.getHp() + drain);
		a.clearTempMods();
		d.clearTempMods();
		for (CombatTrigger t : aSuccess.keySet()) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_POST)!=0) {
				t.runPostAttack(this, leftAttacking, a, d, damage, currentEffect);
			}
		}
		for (CombatTrigger t : dSuccess.keySet()) {
			if (dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_POST)!=0) {
				t.runPostAttack(this, leftAttacking, a, d, damage, currentEffect);
			}
		}
		
	}

	public void addToAttackQueue(Unit a, Unit d, String animation, int damage, int drain) {
		AttackRecord rec = new AttackRecord();
		rec.attacker = new UnitIdentifier(a);
		rec.defender = new UnitIdentifier(d);
		rec.animation = animation;
		rec.damage = damage;
		rec.drain = drain;
		attackQueue.add(rec);

		System.out.println(animation + ": " + a.name + ", " + d.name + ", "
				+ damage + ", " + drain + " (drain)");
	}
	
	public ArrayList<AttackRecord> getAttackQueue(){
		return attackQueue;
	}
	
	public static int calculateBaseDamage(Unit a, Unit d){
		boolean effective = a.getWeapon().effective.contains(d.getTheClass().name)
				|| (d.getTheClass().name.equals("Lord") 
						&& a.getWeapon().effective.contains(d.name));
		if (a.getWeapon().isMagic()) {
			return a.get("Mag")
					+ (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon()))
					* (effective ? 3: 1) - d.get("Res");
		} else {
			return  a.get("Str")
					+ (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon()))
					* (effective? 3:1) - d.get("Def");
		}
	}
	
}
