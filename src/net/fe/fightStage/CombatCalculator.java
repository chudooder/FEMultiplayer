package net.fe.fightStage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

import net.fe.FEMultiplayer;
import net.fe.RNG;
import net.fe.network.FEServer;
import net.fe.overworldStage.Grid;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Weapon;

public class CombatCalculator {
	private Unit left, right;
	private ArrayList<AttackRecord> attackQueue;
	private int range;
	private Queue<String> nextAttack;
	public CombatCalculator(UnitIdentifier u1, UnitIdentifier u2, boolean local){
		
		if(local){
			left = FEMultiplayer.getUnit(u1);
			right = FEMultiplayer.getUnit(u2);
		} else {
			left = FEServer.getUnit(u1);
			right = FEServer.getUnit(u2);
		}
//		System.out.println(left);
//		System.out.println(right);
		range = Grid.getDistance(left, right);
		attackQueue = new ArrayList<AttackRecord>();
		nextAttack = new LinkedList<String>();
		calculate();
	}
	private void calculate() {
		// Determine turn order
		ArrayList<Boolean> attackOrder = new ArrayList<Boolean>();
		if (shouldAttack(left,right,range))
			attackOrder.add(true);
		if (shouldAttack(right,left,range))
			attackOrder.add(false);
		if (left.get("Spd") >= right.get("Spd") + 4 
				&& shouldAttack(left,right,range)) {
			attackOrder.add(true);
		}
		if (right.get("Spd") >= left.get("Spd") + 4
				&& shouldAttack(right,left,range)) {
			attackOrder.add(false);
		}
		System.out.println(attackOrder);

		for (Boolean i : attackOrder) {
			attack(i, "None");
			while(!nextAttack.isEmpty()){
				attack(i, nextAttack.poll());
			}
		}
	}
	
	public static boolean shouldAttack(Unit a, Unit d, int range){
		if(a.getWeapon() == null) return false;
		if(a.getWeapon().getUses() == 0) return false;
		if(!a.getWeapon().range.contains(range)) return false;
		if(a.getWeapon().type == Weapon.Type.STAFF) return false;
		return true;
	}
	
	public void addAttack(String effect){
		nextAttack.add(effect);
	}

	private void attack(boolean leftAttacking, String currentEffect) {
		Unit a = leftAttacking?left: right;
		Unit d = leftAttacking?right: left;
		if(!shouldAttack(a, d, range)) return;
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
			aSuccess.put(t,t.attempt(a, range));
		}
		for (CombatTrigger t : d.getTriggers()) {
			dSuccess.put(t,t.attempt(d, range));
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
		
		
		if (!((RNG.get()+RNG.get())/2 < hitRate(a, d))) {
			miss = true;
			if (a.getWeapon().isMagic())
				a.use(a.getWeapon(), false); //TODO Remove on server
		} else {
			a.use(a.getWeapon(), false); //TODO Remove on server
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
		boolean effective = a.getWeapon().effective.contains(d.functionalClassName());
		int base;
		if (a.getWeapon().isMagic()) {
			base = a.get("Mag")
					+ (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon()))
					* (effective ? 3: 1) - d.get("Res");
		} else {
			base = a.get("Str")
					+ (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon()))
					* (effective? 3:1) - d.get("Def");
		}
		return Math.max(base, 0);
	}
	
	public static int hitRate(Unit a, Unit d){
		return a.hit() - d.avoid() + a.getWeapon().triMod(d.getWeapon()) * 15;
	}
	
}
