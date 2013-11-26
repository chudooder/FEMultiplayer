package net.fe.fightStage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import net.fe.RNG;
import net.fe.overworldStage.Grid;
import net.fe.unit.Unit;
import chu.engine.Entity;
import chu.engine.Stage;

public class FightStage extends Stage {
	private Unit left, right;
	private FightUnit fl, fr;
	private ArrayList<AttackRecord> attackQueue;
	private int currentEvent;
	
	public static final int START = 0;
	public static final int ATTACKING = 1;
	public static final int ATTACKED = 2;
	public static final int RETURNING = 3;
	public static final int DONE = 4;

	public FightStage(Unit u1, Unit u2) {
		attackQueue = new ArrayList<AttackRecord>();
		left = u1;
		right = u2;
		calculate(Grid.getDistance(u1, u2));
	}

	public void calculate(int range) {
		// Determine turn order
		ArrayList<Boolean> attackOrder = new ArrayList<Boolean>();
		if (left.getWeapon() != null && left.getWeapon().range.contains(range))
			attackOrder.add(true);
		if (right.getWeapon() != null
				&& right.getWeapon().range.contains(range))
			attackOrder.add(false);
		if (left.get("Spd") >= right.get("Spd") + 4 && left.getWeapon() != null
				&& left.getWeapon().range.contains(range)) {
			attackOrder.add(true);
		}
		if (right.get("Spd") >= left.get("Spd") + 4
				&& right.getWeapon() != null
				&& right.getWeapon().range.contains(range)) {
			attackOrder.add(false);
		}

		System.out.println("Battle!\n" + left + "\n" + right);
		for (Boolean i : attackOrder) {
			attack(i, true, "Attack");
		}
	}

	public void attack(boolean dir, boolean skills, String name) {
		Unit a, d;
		int damage = 0;
		String animation = "Attack";
		if (dir) {
			a = left;
			d = right;
		} else {
			a = right;
			d = left;
		}

		if (a.getHp() == 0 || d.getHp() == 0) {
			return;
		}

		ArrayList<CombatTrigger> aTriggers = a.getTriggers();
		ArrayList<CombatTrigger> dTriggers = d.getTriggers();

		for (CombatTrigger t : aTriggers) {
			t.attempt(a);
		}
		for (CombatTrigger t : dTriggers) {
			t.attempt(a);
		}

		if (!(RNG.get() < a.hit() - d.avoid()
				+ a.getWeapon().triMod(d.getWeapon()) * 10)) {
			// Miss
			addToAttackQueue(a, d, "Miss", 0);
			if (a.getWeapon().isMagic())
				a.getWeapon().use(a);
			return;
		}

		if (skills) {
			for (CombatTrigger t : aTriggers) {
				if (t.success) {
					if(!t.runPreAttack(this, a, d)){
						for (CombatTrigger t2 : aTriggers) {
							t2.clear();
						}
						for (CombatTrigger t2 : dTriggers) {
							t2.clear();
						}
						return;
					}
					animation = t.getClass().getSimpleName();
				}
			}
		}

		int crit = 1;
		if (RNG.get() < a.crit() - d.dodge()) {
			crit = 3;
		}

		if (a.getWeapon().isMagic()) {
			damage = a.get("Mag")
					+ (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon()))
					* (a.getWeapon().effective.contains(d.getTheClass()) ? 3
							: 1) - d.get("Res");
		} else {
			damage = a.get("Str")
					+ (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon()))
					* (a.getWeapon().effective.contains(d.getTheClass()) ? 3
							: 1) - d.get("Def");
		}
		damage *= crit;

		for (CombatTrigger t : dTriggers) {
			if (t.success) {
				damage = t.runDamageMod(damage);
			}
		}
		damage = Math.min(damage, d.getHp());

		if (crit == 3) {
			animation += " Critical";
		}
		addToAttackQueue(a, d, animation, damage);
		d.setHp(d.getHp() - damage);
		a.clearTempMods();
		d.clearTempMods();
		if (skills) {
			for (CombatTrigger t : aTriggers) {
				if (t.success) {
					t.runPostAttack(this, a, d, damage);
				}
			}
		}
	}

	/**
	 * Adds an attack to the Attack Queue
	 * 
	 * @param a
	 *            The attacking unit
	 * @param d
	 *            The defending unit
	 * @param animation
	 *            The name of the animation to play
	 * @param damage
	 *            The damage of the attack on the defending unit
	 * @param consume
	 *            The number of attacks in the animation. If consume > 1, it
	 *            will get the next (consume - 1) AttackRecords in the
	 *            attackQueue.
	 */
	public void addToAttackQueue(Unit a, Unit d, String animation, int damage) {
		AttackRecord rec = new AttackRecord();
		rec.attacker = a;
		rec.defender = d;
		rec.animation = animation;
		rec.damage = damage;
		attackQueue.add(rec);
	}

	@Override
	public void beginStep() {
		for (Entity e : entities) {
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
	}

	@Override
	public void onStep() {
		for (Entity e : entities) {
			e.onStep();
		}
		processAddStack();
		processRemoveStack();
		if(attackQueue.size()!=0){
			processAttackQueue();
		} else {
			//TODO switch back to the other stage
			System.out.println(left.name + " HP:" + left.getHp() + " | " + right.name + 
					" HP:" + right.getHp());
			System.exit(0);
		}
	}
	
	private void processAttackQueue(){
		final AttackRecord rec = attackQueue.get(0);
		FightUnit a, d;
		if(rec.attacker == right) {
			a = fr;
			d = fl;
		} else {
			a = fl;
			d = fr;
		}
		if(currentEvent == START){
			System.out.println("\n" + rec.attacker.name + "'s turn!");
			currentEvent = ATTACKING;
			a.sprite.setSpeed(50);
		} else if (currentEvent == ATTACKING){
			//Let the animation play
		} else if (currentEvent == ATTACKED){
			if(rec.animation.equals("Miss")){
				//TODO Play defenders dodge animation
				System.out.println("Miss! " + rec.defender.name + " dodged the attack!");
			} else {
				//TODO Play healthbar going down/up animation
				System.out.println(rec.animation + "! " + rec.defender.name + 
						" took " + rec.damage +	" damage!");
			}
			currentEvent = RETURNING;
		} else if (currentEvent == RETURNING){
			//Let animation play
		} else if (currentEvent == DONE){
			currentEvent = START;
			attackQueue.remove(0);
		}
	}

	@Override
	public void endStep() {
		for (Entity e : entities) {
			e.endStep();
		}
		processAddStack();
		processRemoveStack();
	}

	private class AttackRecord {
		public String animation;
		public Unit attacker, defender;
		public int damage;
	}

	public void setCurrentEvent(int event) {
		currentEvent = event;
	}

}
