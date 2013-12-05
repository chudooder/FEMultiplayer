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
import chu.engine.anim.Renderer;

public class FightStage extends Stage {
	private Unit left, right;
	private FightUnit leftFighter, rightFighter;
	private Healthbar leftHP, rightHP;
	private int range;
	
	private CombatCalculator calc;
	
	private Texture bg;
	private int currentEvent;
	private float prevShakeTimer;
	private float shakeTimer;
	private float shakeX;
	private float shakeY;
	
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
		left = u1;
		right = u2;
		leftFighter = new FightUnit(new AnimationArgs(left, true, range), this);
		rightFighter = new FightUnit(new AnimationArgs(right, false, range), this);
		leftHP = new Healthbar(left, true);
		rightHP = new Healthbar(right, false);
		addEntity(leftFighter);
		addEntity(rightFighter);
		addEntity(leftHP);
		addEntity(rightHP);
		addEntity(new Platform(u1.getTerrain(), true, range));
		addEntity(new Platform(u1.getTerrain(), false, range));
		addEntity(new HUD(u1, u2, this));
		addEntity(new HUD(u2, u1, this));
		bg = Resources.getTexture(u2.getTerrain().toString().toLowerCase() + "_bg");
		
		System.out.println("Battle!\n" + left + "\n" + right + "\n");
		System.out.println("Running calcuations:");
		calc = new CombatCalculator(u1, u2);
		calc.calculate(range);
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
		if (calc.getAttackQueue().size() != 0) {
			processAttackQueue();
		} else {
			// TODO switch back to the other stage
			System.out.println(left.name + " HP:" + left.getHp() + " | "
					+ right.name + " HP:" + right.getHp());
			System.exit(0);
		}
	}

	private void processAttackQueue() {
		final AttackRecord rec = calc.getAttackQueue().get(0);
		FightUnit a = rec.attacker == right? rightFighter: leftFighter;
		FightUnit d = rec.attacker == right? leftFighter: rightFighter;
		Healthbar dhp = rec.defender == left? leftHP: rightHP;
		boolean crit = rec.animation.contains("Critical");		
		
		if (currentEvent == START) {
			System.out.println("\n" + rec.attacker.name + "'s turn!"); //Debug
			ArrayList<String> messages = getMessages(rec.animation, "(a)");
			for(int i = 0; i < messages.size(); i++){
				addEntity(new Message(messages.get(i), rec.attacker == left, i));
			}
			a.sprite.setAnimation(crit?"CRIT":"ATTACK");
			a.sprite.setSpeed(AttackAnimation.NORMAL_SPEED);
			
			currentEvent = ATTACKING;
		} else if (currentEvent == ATTACKING) {
			// Let the animation play
			
			
		} else if (currentEvent == ATTACKED) {
			if (rec.animation.equals("Miss")) {
				System.out.println("Miss! " + rec.defender.name
						+ " dodged the attack!");
				
				d.sprite.setAnimation("DODGE");
				d.sprite.setFrame(0);
				d.sprite.setSpeed(DodgeAnimation.NORMAL_SPEED);
				addEntity(new MissEffect(rec.defender == left));
				if(rec.attacker.getWeapon().isMagic()){
					addEntity(a.getHitEffect(crit));
				}
				
				currentEvent = HURTED;
			} else {
				System.out.println(rec.animation + "! " + rec.defender.name
						+ " took " + rec.damage + " damage!");
				
				dhp.setHp(dhp.getHp() - rec.damage);
				addEntity(a.getHitEffect(crit));
				startShaking(crit?1.2f:.5f);
				
				
				if(rec.damage != 0) {
					currentEvent = HURTING;
				} else {
					currentEvent = HURTED;
				}
			}
			
			ArrayList<String> messages = getMessages(rec.animation, "(d)");
			for(int i = 0; i < messages.size(); i++){
				addEntity(new Message(messages.get(i), rec.attacker == left, i));
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
			
			
		} else if (currentEvent == DONE) {
			calc.getAttackQueue().remove(0);
			
			currentEvent = START;
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
	
	public int getRange(){
		return range;
	}
	
	public boolean isLeft(Unit u){
		return u == left;
	}
	
	public static int rangeToHeadDistance(int range) {
		if (range == 1) {
			return 32;
		} else {
			return 54;
		}
	}

	
	
	
}
