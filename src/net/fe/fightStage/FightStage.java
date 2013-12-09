package net.fe.fightStage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Timer;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import net.fe.FEMultiplayer;
import net.fe.RNG;
import net.fe.fightStage.anim.*;
import net.fe.overworldStage.Grid;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Weapon;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Resources;
import chu.engine.Stage;
import chu.engine.anim.Animation;
import chu.engine.anim.Renderer;

public class FightStage extends Stage {
	private Unit left, right;
	private FightUnit leftFighter, rightFighter;
	private Healthbar leftHP, rightHP;
	private int range;
	
	private ArrayList<AttackRecord> attackQ;
	
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
	public static final int DYING = 5;
	public static final int RETURNING = 6;
	public static final int DONE = 7;

	public FightStage(UnitIdentifier u1, UnitIdentifier u2, ArrayList<AttackRecord> attackQ) {
		shakeTimer = 0;
		prevShakeTimer = 0;
		shakeX = 0;
		shakeY = 0;
		left = FEMultiplayer.getUnit(u1);
		right = FEMultiplayer.getUnit(u2);
		
		range = Grid.getDistance(left, right);
		leftFighter = new FightUnit(new AnimationArgs(left, true, range), this);
		rightFighter = new FightUnit(new AnimationArgs(right, false, range), this);
		leftHP = new Healthbar(left, true);
		rightHP = new Healthbar(right, false);
		addEntity(leftFighter);
		addEntity(rightFighter);
		addEntity(leftHP);
		addEntity(rightHP);
		addEntity(new Platform(left.getTerrain(), true, range));
		addEntity(new Platform(right.getTerrain(), false, range));
		addEntity(new HUD(left, right, this));
		addEntity(new HUD(right, left, this));
		bg = Resources.getTexture(right.getTerrain().toString().toLowerCase() + "_bg");
		
		this.attackQ = attackQ;
		
		System.out.println("-------Battle!-------\n" + left + "\n" + right + "\n");
		System.out.println("Running calcuations:");
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
		if (attackQ.size() != 0) {
			processAttackQueue();
		} else {
			System.out.println(left.name + " HP:" + left.getHp() + " | "
					+ right.name + " HP:" + right.getHp());
			FEMultiplayer.setCurrentStage(FEMultiplayer.getOverworldStage());
		}
	}

	private void processAttackQueue() {
		final AttackRecord rec = attackQ.get(0);
		Unit attacker = FEMultiplayer.getUnit(rec.attacker);
		Unit defender = FEMultiplayer.getUnit(rec.defender);
		
		FightUnit a = attacker == right? rightFighter: leftFighter;
		FightUnit d = attacker == right? leftFighter: rightFighter;
		Healthbar dhp = defender == left? leftHP: rightHP;
		Healthbar ahp = defender == left? rightHP: leftHP;
		
		boolean crit = rec.animation.contains("Critical");		
		a.renderDepth = FightStage.UNIT_DEPTH;
		d.renderDepth = FightStage.UNIT_DEPTH+0.01f;
		
		
		if (currentEvent == START) {
			System.out.println("\n" + rec.attacker.name + "'s turn!"); //Debug
			ArrayList<String> messages = getMessages(rec.animation, "(a)");
			for(int i = 0; i < messages.size(); i++){
				addEntity(new Message(messages.get(i), attacker == left, i));
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
				addEntity(new MissEffect(defender == left));
				if(attacker.getWeapon().isMagic()){
					addEntity(a.getHitEffect(crit));
				}
				
				currentEvent = HURTED;
			} else {
				System.out.println(rec.animation + "! " + rec.defender.name
						+ " took " + rec.damage + " damage!");
				
				defender.setHp(defender.getHp() - rec.damage);
				attacker.setHp(attacker.getHp() + rec.drain);
				dhp.setHp(dhp.getHp() - rec.damage);
				ahp.setHp(ahp.getHp() + rec.drain, false);
				addEntity(a.getHitEffect(crit));
				startShaking(crit?1.3f:.5f);
				
				
				if(rec.damage != 0) {
					currentEvent = HURTING;
				} else {
					currentEvent = HURTED;
				}
			}
			
			ArrayList<String> messages = getMessages(rec.animation, "(d)");
			for(int i = 0; i < messages.size(); i++){
				addEntity(new Message(messages.get(i), attacker == left, i));
			}
		} else if (currentEvent == HURTING) {
			// let health bar animation play
			
				
		} else if (currentEvent == HURTED) {
			if (dhp.getHp() == 0) {
				d.dying = true;
				currentEvent = DYING;
			} else {
				currentEvent = RETURNING;
			}
		} else if (currentEvent == DYING) {
			// Let animation for dying guy play
		} else if (currentEvent == RETURNING) {
			a.sprite.setSpeed(AttackAnimation.NORMAL_SPEED);
			// Let animation play
		} else if (currentEvent == DONE) {
			attackQ.remove(0);
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
				float factor = Math.min(shakeTimer*1.2f, 1.0f);
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
		float dist = Math.min(shakeTimer*9, 5);
		shakeX = -dist;
		shakeY = -dist;
	}
	
	//Getters Setters

	public void setCurrentEvent(int event) {
		if(currentEvent == ATTACKING && event == HURTED)
			return;
		if((event == HURTED || event == HURTING || event == currentEvent + 1)){
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
