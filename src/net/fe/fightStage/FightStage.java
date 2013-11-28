package net.fe.fightStage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import net.fe.RNG;
import net.fe.overworldStage.Grid;
import net.fe.unit.Unit;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Resources;
import chu.engine.Stage;
import chu.engine.anim.Animation;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;

public class FightStage extends Stage {
	private Unit left, right;
	private FightUnit fl, fr;
	private Healthbar hp1, hp2;
	private ArrayList<AttackRecord> attackQueue;
	private int currentEvent;
	private int range;
	private float prevShakeTimer;
	private float shakeTimer;
	private float shakeX;
	private float shakeY;

	// Config
	public static final float SHAKE_INTERVAL = 0.05f;
	
	public static final int CENTRAL_AXIS = 120;
	public static final int FLOOR = 104;

	public static final int START = 0;
	public static final int ATTACKING = 1;
	public static final int ATTACKED = 2;
	public static final int HURTING = 3;
	public static final int HURTED = 4;
	public static final int RETURNING = 5;
	public static final int DONE = 6;

	public FightStage(Unit u1, Unit u2) {
		shakeTimer = 0;
		prevShakeTimer = 0;
		shakeX = 0;
		shakeY = 0;
		range = Grid.getDistance(u1, u2);
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
			attack(i);
		}
	}

	public void attack(boolean dir) {
		Unit a, d;
		int damage = 0;
		String animation = "Attack";
		boolean miss = false;
		int crit = 1;
		
		
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
		LinkedHashMap<CombatTrigger, Boolean> aSuccess = 
				new LinkedHashMap<CombatTrigger, Boolean>();
		ArrayList<CombatTrigger> dTriggers = d.getTriggers();
		LinkedHashMap<CombatTrigger, Boolean> dSuccess = 
				new LinkedHashMap<CombatTrigger, Boolean>();
		for (CombatTrigger t : aTriggers) {
			aSuccess.put(t,t.attempt(a));
		}
		for (CombatTrigger t : dTriggers) {
			dSuccess.put(t,t.attempt(a));
		}
		
		

		for (CombatTrigger t : aTriggers) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_PRE)!=0) {
				if (t.nameModification == CombatTrigger.REPLACE_NAME_AFTER_PRE) {
					t.runPreAttack(this, a, d);
					animation = t.getName() + "(a)";
				}
			}
		}	
		for (CombatTrigger t : dTriggers) {
			if (dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_PRE)!=0) {
				if (t.nameModification == CombatTrigger.REPLACE_NAME_AFTER_PRE) {
					t.runPreAttack(this, a, d);
					animation = t.getName() + "(d)";
				}
			}
		}
		
		
		
		if (!(RNG.get() < a.hit() - d.avoid()
				+ a.getWeapon().triMod(d.getWeapon()) * 10)) {
			miss = true;
			if (a.getWeapon().isMagic())
				a.getWeapon().use(a);
		}
		if (RNG.get() < a.crit() - d.dodge()) {
			crit = 3;
			animation += " Critical(a)";
		}
		
		
		damage = calculateBaseDamage(a, d) * crit;
		for (CombatTrigger t : aTriggers) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_MOD)!=0) {
				int oldDamage = damage;
				damage = t.runDamageMod(a,d,damage);
				if (t.nameModification == CombatTrigger.APPEND_NAME_AFTER_MOD 
						&& damage!=oldDamage) {
					animation += " " + t.getName() + "(a)";
				}
			}
		}
		for (CombatTrigger t : dTriggers) {
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
		
		
		if(miss){
			damage = 0;
			animation = "Miss";
		}

		addToAttackQueue(a, d, animation, damage);
		d.setHp(d.getHp() - damage);
		a.clearTempMods();
		d.clearTempMods();
		for (CombatTrigger t : aTriggers) {
			if (aSuccess.get(t) && (t.turnToRun & CombatTrigger.YOUR_TURN_POST)!=0) {
				t.runPostAttack(this, dir, a, d, damage);
			}
		}
		for (CombatTrigger t : dTriggers) {
			if (dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_POST)!=0) {
				t.runPostAttack(this, dir, a, d, damage);
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
		FightUnit d;
		boolean crit = rec.animation.contains("Critical");
		String hitEffect = crit?"crit":"attack";
		
		Healthbar dhp;
		if (rec.attacker == right) {
			a = fr;
			d = fl;
		} else {
			a = fl;
			d = fr;
		}
		
		if (rec.defender == left){
			dhp = hp1;
		} else {
			dhp = hp2;
		}
		
		if (currentEvent == START) {
			System.out.println("\n" + rec.attacker.name + "'s turn!");
			currentEvent = ATTACKING;
			if (crit)
				a.sprite.setAnimation("CRIT");
			else
				a.sprite.setAnimation("ATTACK");
			a.sprite.setSpeed(40);
		} else if (currentEvent == ATTACKING) {
			// Let the animation play
		} else if (currentEvent == ATTACKED) {
			if (rec.animation.equals("Miss")) {
				// TODO Play defenders dodge animation
				System.out.println("Miss! " + rec.defender.name
						+ " dodged the attack!");
				currentEvent = RETURNING;
			} else {
				dhp.setHp(dhp.getHp() - rec.damage);
				addEntity(new HitEffect(hitEffect, rec.attacker == left));
				if(crit) {
					startShaking(1.2f);
				} else {
					startShaking(.5f);
				}
				System.out.println(rec.animation + "! " + rec.defender.name
						+ " took " + rec.damage + " damage!");
				if(rec.damage != 0) {
					currentEvent = HURTING;
				} else {
					currentEvent = RETURNING;
				}
			}
			if (dhp.getHp() == 0) {
				d.dying = true;
			}
		} else if (currentEvent == HURTING) {
			// let health bar animation play
		} else if (currentEvent == HURTED) {
			a.sprite.setSpeed(40);
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
		
		if(shakeTimer > 0) {
			shakeTimer -= Game.getDeltaSeconds();
			if(prevShakeTimer - shakeTimer > SHAKE_INTERVAL) {
				float factor = Math.min(shakeTimer, 1.0f);
				shakeX *= -factor;
				shakeY *= -factor;
				prevShakeTimer = shakeTimer;
			}
			if(shakeTimer < 0) {
				shakeTimer = 0;
				prevShakeTimer = 0;
				shakeX = 0;
				shakeY = 0;
			}
		}
		
		//Shake
		Renderer.translate((int)shakeX, (int)shakeY);
		
		List<Unit> units = Arrays.asList(left, right);
		for(int i = 0; i < units.size(); i++){
			int sign = i*2-1;
			
			Unit u1 = units.get(i);
			Unit u2 = units.get((i + 1) %2);
			
			//Main status
			Renderer.drawRectangle(CENTRAL_AXIS + sign*120, FLOOR + 14, 
					CENTRAL_AXIS, FLOOR + 56, 0, borderDark);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*119, FLOOR + 15, 
					CENTRAL_AXIS + sign, FLOOR + 55, 0, borderLight);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*118, FLOOR + 16, 
					CENTRAL_AXIS + sign*2, FLOOR + 54, 0, 
					u1.getTeamColor().darker(0.5f));
			
			//Weapon
			Renderer.drawRectangle(CENTRAL_AXIS + sign*119, FLOOR + 15, 
					CENTRAL_AXIS + sign, FLOOR+31, 0, borderLight);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*118, FLOOR + 16, 
					CENTRAL_AXIS + sign*2, FLOOR+30, 0, new Color(0xb0a878));
			Renderer.drawString("default_small" , u1.getWeapon().name,
					CENTRAL_AXIS + sign*39 - 20, FLOOR + 17);
			
			//Attack Stats
			Renderer.drawRectangle(CENTRAL_AXIS + sign*120, FLOOR - 1,
					CENTRAL_AXIS + sign*76, FLOOR + 32, 0, borderDark);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*119, FLOOR ,
					CENTRAL_AXIS + sign*77, FLOOR + 31, 0, borderLight);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*118, FLOOR + 1,
					CENTRAL_AXIS + sign*78, FLOOR + 30, 0, 
					u1.getTeamColor());
			
			String hit, crit, dmg;
			
			if(!u1.getWeapon().range.contains(range)){
				hit = "  -";
				crit = "  -";
				dmg = "  -";
			} else {
				hit = String.format("%3d", 
						Math.min(100, Math.max(u1.hit()-u2.avoid(),0)));
				crit = String.format("%3d", 
						Math.min(100, Math.max(u1.crit()-u2.dodge(),0)));
				dmg = String.format("%3d", 
						Math.min(100, Math.max(calculateBaseDamage(u1,u2),0)));
			}
			
			Renderer.drawString("default_small", "HIT", 
					CENTRAL_AXIS + sign*98 - 18, FLOOR);
			Renderer.drawString("number", hit, 
					CENTRAL_AXIS + sign*98, FLOOR);
			
			Renderer.drawString("default_small", "CRT", 
					CENTRAL_AXIS + sign*98 - 18, FLOOR + 9);
			Renderer.drawString("number", crit, 
					CENTRAL_AXIS + sign*98, FLOOR + 9);
			
			Renderer.drawString("default_small", "DMG", 
					CENTRAL_AXIS + sign*98 - 18, FLOOR + 18);
			Renderer.drawString("number", dmg, 
					CENTRAL_AXIS + sign*98, FLOOR + 18);
			
			//Name
			Renderer.drawRectangle(CENTRAL_AXIS + sign*120, FLOOR -99, 
					CENTRAL_AXIS + sign*63, FLOOR-77, 0, borderDark);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*120, FLOOR -98, 
					CENTRAL_AXIS + sign*64, FLOOR-78, 0, borderLight);
			Renderer.drawRectangle(CENTRAL_AXIS + sign*120, FLOOR -97, 
					CENTRAL_AXIS + sign*65, FLOOR-79, 0, 
					u1.getTeamColor());
			Renderer.drawString("default", units.get(i).name, 
					CENTRAL_AXIS + sign*94 - 16, FLOOR - 95);
			
		}
		
		super.render();
		
		//Undo shake translation
		Renderer.translate((int)-shakeX, (int)-shakeY);
	}

	public static int calculateBaseDamage(Unit a, Unit d){
		if (a.getWeapon().isMagic()) {
			return a.get("Mag")
					+ (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon()))
					* (a.getWeapon().effective.contains(d.getTheClass()) ? 3
							: 1) - d.get("Res");
		} else {
			return  a.get("Str")
					+ (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon()))
					* (a.getWeapon().effective.contains(d.getTheClass()) ? 3
							: 1) - d.get("Def");
		}
	}
	
	private void startShaking(float time) {
		shakeTimer = time;
		prevShakeTimer = time;
		float dist = Math.min(shakeTimer*9, 7);
		shakeX = -dist;
		shakeY = -dist;
	}
	
	// On regular hit
	
}
