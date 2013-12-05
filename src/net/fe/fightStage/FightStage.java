package net.fe.fightStage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import net.fe.RNG;
import net.fe.fightStage.anim.*;
import net.fe.overworldStage.Grid;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Resources;
import chu.engine.Stage;
import chu.engine.anim.Animation;
import chu.engine.anim.Renderer;

public class FightStage extends Stage {
	private Unit left, right;
	private FightUnit fl, fr;
	private Healthbar hp1, hp2;
	private Texture bg;
	private ArrayList<AttackRecord> attackQueue;
	private int currentEvent;
	private int range;
	private float prevShakeTimer;
	private float shakeTimer;
	private float shakeX;
	private float shakeY;
	private float scrollX;
	
	// Config
	public static final float SHAKE_INTERVAL = 0.05f;
	
	public static final int CENTRAL_AXIS = 120;
	public static final int FLOOR = 104;
	
	public static Color BORDER_DARK = new Color(0x483828);
	public static Color BORDER_LIGHT = new Color(0xf8f0c8);
	public static Color NEUTRAL = new Color(0xb0a878);
	
	public static final float HP_DEPTH = 0;
	public static final float HUD_DEPTH = 0.1f;
	public static final float EFFECT_DEPTH = 0.2f;
	public static final float UNIT_DEPTH = 0.5f;
	public static final float PLATFORM_DEPTH = 0.7f;
	public static final float BG_DEPTH = 1;

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
		fl = new FightUnit(new AnimationArgs(left, true, range), this);
		fr = new FightUnit(new AnimationArgs(right, false, range), this);
		hp1 = new Healthbar(left, true);
		hp2 = new Healthbar(right, false);
		addEntity(fl);
		addEntity(fr);
		addEntity(hp1);
		addEntity(hp2);
		addEntity(new Platform(u1.getTerrain(), true, range));
		addEntity(new Platform(u1.getTerrain(), false, range));
		addEntity(new HUD(u1, u2, this));
		addEntity(new HUD(u2, u1, this));
		bg = Resources.getTexture(u2.getTerrain().toString().toLowerCase() + "_bg");
		
		System.out.println("Battle!\n" + left + "\n" + right + "\n");
		System.out.println("Running calcuations:");
		calculate(range);
	}

	public void calculate(int range) {
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
	}
	
	public static boolean shouldAttack(Unit a, Unit d, int range, boolean first){
		if(a.getWeapon() == null) return false;
		if(!a.getWeapon().range.contains(range)) return false;
		if(a.getWeapon().type == Weapon.Type.STAFF && !first) return false;
		if((a.getWeapon().type == Weapon.Type.STAFF)
				!= (a.getPartyColor().equals(d.getPartyColor()))) return false;
		return true;
	}

	public void attack(boolean dir, String currentEffect) {
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
				t.runPostAttack(this, dir, a, d, damage, currentEffect);
			}
		}
		for (CombatTrigger t : dTriggers) {
			if (dSuccess.get(t) && (t.turnToRun & CombatTrigger.ENEMY_TURN_POST)!=0) {
				t.runPostAttack(this, dir, a, d, damage, currentEffect);
			}
		}
		
	}

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
			ArrayList<String> messages = getMessages(rec.animation, "(a)");
			for(int i = 0; i < messages.size(); i++){
				addEntity(new Message(messages.get(i), rec.attacker == left, i));
			}
			currentEvent = ATTACKING;
			if (crit)
				a.sprite.setAnimation("CRIT");
			else
				a.sprite.setAnimation("ATTACK");
			a.sprite.setSpeed(AttackAnimation.NORMAL_SPEED);
		} else if (currentEvent == ATTACKING) {
			// Let the animation play
			if(range > 1) {
				Animation anim = a.sprite.getCurrentAnimation();
				int diff = ((AttackAnimation)anim).getNextHitFrame() - anim.getFrame();
				if(diff > 0 && diff < 2) {
					scrollX += (rec.defender==left?300:-300)*Game.getDeltaSeconds();
				}
			}
		} else if (currentEvent == ATTACKED) {
			ArrayList<String> messages = getMessages(rec.animation, "(d)");
			for(int i = 0; i < messages.size(); i++){
				addEntity(new Message(messages.get(i), rec.attacker == left, i));
			}
			if (rec.animation.equals("Miss")) {
				// TODO Play defenders dodge animation
				System.out.println("Miss! " + rec.defender.name
						+ " dodged the attack!");
				currentEvent = HURTED;
				d.sprite.setAnimation("DODGE");
				d.sprite.setFrame(0);
				d.sprite.setSpeed(DodgeAnimation.NORMAL_SPEED);
				addEntity(new MissEffect(rec.defender == left));
				if(rec.attacker.getWeapon().isMagic()){
					addEntity(a.getHitEffect(crit));
				}
			} else {
				dhp.setHp(dhp.getHp() - rec.damage);
				addEntity(a.getHitEffect(crit));
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
					currentEvent = HURTED;
				}
			}
		} else if (currentEvent == HURTING) {
			// let health bar animation play
		} else if (currentEvent == HURTED) {
			if (dhp.getHp() == 0) {
				d.dying = true;
			}
			a.sprite.setSpeed(AttackAnimation.NORMAL_SPEED);
			currentEvent = RETURNING;
		} else if (currentEvent == RETURNING) {
			// Let animation play
			if(range > 1) {
				Animation anim = a.sprite.getCurrentAnimation();
				int diff = anim.getLength() - anim.getFrame();
				if(diff > 0 && diff < 2) {
					scrollX -= (rec.defender==left?300:-300)*Game.getDeltaSeconds();
				}
			}
		} else if (currentEvent == DONE) {
			currentEvent = START;
			scrollX = 0;
			attackQueue.remove(0);
		}
	}
	
	private ArrayList<String> getMessages(String animationName, String suffix){
		ArrayList<String> ans = new ArrayList<String>();
		String[] animation = animationName.split(" ");
		for(String anim: animation){
			if(anim.endsWith(suffix)){
				anim = anim.substring(0, anim.length()-suffix.length());
			} else {
				continue;
			}
			if(anim.matches(".*\\d")){
				if(anim.endsWith("1")){
					anim = anim.substring(0, anim.length()-1);
				} else {
					continue;
				}
			}
			ans.add(anim.toUpperCase());
		}
		return ans;
	}
	
	public void render() {
		Renderer.pushMatrix();
		Renderer.scale(2, 2);
		Renderer.render(bg, 0, 0, 1, 1, 0, 0, 240, 160, 1);
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
		
		super.render();
		
		//Undo shake translation
		Renderer.popMatrix();
		Renderer.removeClip();
	}

	@Override
	public void endStep() {
		for (Entity e : entities) {
			e.endStep();
		}
		processAddStack();
		processRemoveStack();
	}
	
	private void startShaking(float time) {
		shakeTimer = time;
		prevShakeTimer = time;
		float dist = Math.min(shakeTimer*9, 7);
		shakeX = -dist;
		shakeY = -dist;
	}
	
	//Getters Setters

	public void setCurrentEvent(int event) {
		if(event == HURTED || event == HURTING || event == currentEvent + 1){
			currentEvent = event;
		} else {
			throw new IllegalArgumentException("Invalid state transit: " + currentEvent + " to " + event);
		}
		
	}
	
	public int distanceToHead(){
		return rangeToHeadDistance(range);
	}

	public static int rangeToHeadDistance(int range) {
		if (range == 1) {
			return 32;
		} else {
			return 54;
		}
	}
	
	public int getRange(){
		return range;
	}
	
	public float getScrollX() {
		return scrollX;
	}
	
	public boolean isLeft(Unit u){
		return u == left;
	}
	
	// On regular hit
	

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
	
	
	private class AttackRecord {
		public String animation;
		public Unit attacker, defender;
		public int damage;
	}
}
