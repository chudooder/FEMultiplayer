package net.fe.fightStage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Color;

import net.fe.RNG;
import net.fe.overworldStage.Grid;
import net.fe.unit.Unit;
import chu.engine.Entity;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class FightStage extends Stage {
	private Unit left, right;
	private FightUnit fl, fr;
	private Healthbar hp1, hp2;
	private ArrayList<AttackRecord> attackQueue;
	private int currentEvent;

	// Config
	public static final int CENTRAL_AXIS = 120;
	public static final int FLOOR = 104;

	public static final int START = 0;
	public static final int ATTACKING = 1;
	public static final int ATTACKED = 2;
	public static final int RETURNING = 3;
	public static final int DONE = 4;

	public FightStage(Unit u1, Unit u2) {
		int range = Grid.getDistance(u1, u2);
		attackQueue = new ArrayList<AttackRecord>();
		left = u1;
		right = u2;
		fl = left.getFightUnit(true, this, rangeToHeadDistance(range));
		fr = right.getFightUnit(false, this, rangeToHeadDistance(range));
		hp1 = left.getHealthbar(true);
		hp2 = right.getHealthbar(false);
		addEntity(fl);
		addEntity(fr);
		addEntity(hp1);
		addEntity(hp2);
		System.out.println("Battle!\n" + left + "\n" + right + "\n");
		System.out.println("Running calcuations:");
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
					if (!t.runPreAttack(this, a, d)) {
						for (CombatTrigger t2 : aTriggers) {
							t2.clear();
						}
						for (CombatTrigger t2 : dTriggers) {
							t2.clear();
						}
						return;
					}
					if (t.nameModification == CombatTrigger.REPLACE_NAME) {
						animation = t.getClass().getSimpleName();
					}
				}
			}
		}

		int crit = 1;
		if (RNG.get() < a.crit() - d.dodge()) {
			crit = 3;
			animation += " Critical";
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
				if (t.nameModification == CombatTrigger.APPEND_NAME) {
					animation += " " + t.getClass().getSimpleName();
				}
			}
		}
		damage = Math.min(damage, d.getHp());

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

		// clear triggers
		for (CombatTrigger t2 : aTriggers) {
			t2.clear();
		}
		for (CombatTrigger t2 : dTriggers) {
			t2.clear();
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

		System.out.println(animation + ": " + a.name + ", " + d.name + ", "
				+ damage);
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
		if (attackQueue.size() != 0) {
			processAttackQueue();
		} else {
			// TODO switch back to the other stage
			System.out.println(left.name + " HP:" + left.getHp() + " | "
					+ right.name + " HP:" + right.getHp());
			System.exit(0);
		}
	}

	private void processAttackQueue() {
		final AttackRecord rec = attackQueue.get(0);
		FightUnit a;
		Healthbar dhp;
		if (rec.attacker == right) {
			a = fr;
			dhp = hp1;
		} else {
			a = fl;
			dhp = hp2;
		}
		if (currentEvent == START) {
			System.out.println("\n" + rec.attacker.name + "'s turn!");
			currentEvent = ATTACKING;
			if (rec.animation.contains("Critical"))
				a.sprite.setAnimation("CRIT");
			else
				a.sprite.setAnimation("ATTACK");
			a.sprite.setSpeed(50);
		} else if (currentEvent == ATTACKING) {
			// Let the animation play
		} else if (currentEvent == ATTACKED) {
			if (rec.animation.equals("Miss")) {
				// TODO Play defenders dodge animation
				System.out.println("Miss! " + rec.defender.name
						+ " dodged the attack!");
			} else {
				dhp.setHp(dhp.getHp() - rec.damage);
				System.out.println(rec.animation + "! " + rec.defender.name
						+ " took " + rec.damage + " damage!");
			}
			if (dhp.getHp() == 0) {
				// TODO Play defender's fading away animation
			}
			currentEvent = RETURNING;
		} else if (currentEvent == RETURNING) {
			// Let animation play
		} else if (currentEvent == DONE) {
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

	public static int rangeToHeadDistance(int range) {
		if (range == 1) {
			return 48;
		}
		return 0;
	}

	public void render() {
		Color borderDark = new Color(0x483828);
		Color borderLight = new Color(0xf8f0c8);
		
		List<Unit> units = Arrays.asList(left, right);
		for(int i = 0; i < units.size(); i++){
			int sign = i*2-1;
			Renderer.drawRectangle(CENTRAL_AXIS + sign*120, FLOOR + 14, 
					CENTRAL_AXIS, FLOOR + 56, 0, borderDark);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*119, FLOOR + 15, 
					CENTRAL_AXIS + sign, FLOOR + 55, 0, borderLight);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*118, FLOOR + 16, 
					CENTRAL_AXIS + sign*2, FLOOR + 54, 0, 
					units.get(i).getTeamColor().darker(0.5f));
			
			Renderer.drawRectangle(CENTRAL_AXIS + sign*119, FLOOR + 15, 
					CENTRAL_AXIS + sign, FLOOR+31, 0, borderLight);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*118, FLOOR + 16, 
					CENTRAL_AXIS + sign*2, FLOOR+30, 0, new Color(0xb0a878));
			
			Renderer.drawRectangle(CENTRAL_AXIS + sign*120, FLOOR + 4,
					CENTRAL_AXIS + sign*76, FLOOR + 32, 0, borderDark);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*119, FLOOR + 5,
					CENTRAL_AXIS + sign*77, FLOOR + 31, 0, borderLight);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*118, FLOOR + 6,
					CENTRAL_AXIS + sign*78, FLOOR + 30, 0, 
					units.get(i).getTeamColor());
			
			
			Renderer.drawRectangle(CENTRAL_AXIS + sign*120, FLOOR -99, 
					CENTRAL_AXIS + sign*63, FLOOR-77, 0, borderDark);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*120, FLOOR -98, 
					CENTRAL_AXIS + sign*64, FLOOR-78, 0, borderLight);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*120, FLOOR -97, 
					CENTRAL_AXIS + sign*65, FLOOR-79, 0, 
					units.get(i).getTeamColor());
		}
		
		super.render();
	}

}
