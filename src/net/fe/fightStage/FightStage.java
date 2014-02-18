package net.fe.fightStage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.fightStage.anim.AnimationArgs;
import net.fe.fightStage.anim.AttackAnimation;
import net.fe.fightStage.anim.BackgroundEffect;
import net.fe.fightStage.anim.DodgeAnimation;
import net.fe.fightStage.anim.HUD;
import net.fe.fightStage.anim.HitEffect;
import net.fe.fightStage.anim.MagicAttack;
import net.fe.fightStage.anim.MagicEffect;
import net.fe.fightStage.anim.MissEffect;
import net.fe.fightStage.anim.NoDamageEffect;
import net.fe.fightStage.anim.Platform;
import net.fe.fightStage.anim.SkillIndicator;
import net.fe.overworldStage.Grid;
import net.fe.transition.FightOverworldTransition;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.SortByRender;
import chu.engine.Stage;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;

public class FightStage extends Stage {
	private Unit left, right;
	private FightUnit leftFighter, rightFighter;
	private Healthbar leftHP, rightHP;
	private int range;
	private boolean done;

	private ArrayList<AttackRecord> attackQ;

	private Texture bg;
	private int currentEvent;
	private float prevShakeTimer;
	private float shakeTimer;
	private float timer;
	private float shakeX;
	private float shakeY;
	private float cameraOffsetF;
	private float cameraOffsetT;
	private float cameraOffset;
	
	private float darknessT, darkness;
	
	public static final HashMap<String, Texture> PRELOADED_EFFECTS = new HashMap<String, Texture>();

	// Config
	public static final float SHAKE_INTERVAL = 0.05f;
	public static final float MIN_TIME = 1.5f;

	public static final int CENTRAL_AXIS = 120;
	public static final int FLOOR = 105;

	public static final Color BORDER_DARK = new Color(0x483828);
	public static final Color BORDER_LIGHT = new Color(0xf8f0c8);
	public static final Color NEUTRAL = new Color(0xb0a878);

	public static final float HP_DEPTH = 0.14f;
	public static final float HUD_DEPTH = 0.15f;
	public static final float EFFECT_DEPTH = 0.2f;
	public static final float UNIT_DEPTH = 0.5f;
	public static final float PLATFORM_DEPTH = 0.7f;
	public static final float BG_DEPTH = 1;

	public static final int START = 0;
	public static final int ATTACKING = 1;
	public static final int HIT_EFFECT = 2;
	public static final int HIT_EFFECTED = 3;
	public static final int ATTACKED = 4;
	public static final int HURTING = 5;
	public static final int HURTED = 6;
	public static final int DYING = 7;
	public static final int RETURNING = 8;
	public static final int DONE = 9;

	public FightStage(UnitIdentifier u1, UnitIdentifier u2,
			ArrayList<AttackRecord> attackQ) {
		super(u1.partyColor.equals(u2.partyColor) ? "curing" :
				u1.partyColor.equals(FEMultiplayer.getLocalPlayer().getParty().getColor()) ?
						"fight" : "defense");
		shakeTimer = 0;
		prevShakeTimer = 0;
		timer = 0;
		done = false;
		shakeX = 0;
		shakeY = 0;
		left = FEMultiplayer.getUnit(u1);
		right = FEMultiplayer.getUnit(u2);
		
//		System.out.println(left);
//		System.out.println(right);

		range = Grid.getDistance(left, right);
		cameraOffsetF = rangeToHeadDistance(range) - rangeToHeadDistance(1);
		leftFighter = new FightUnit(new AnimationArgs(left, true, range), this);
		rightFighter = new FightUnit(new AnimationArgs(right, false, range),
				this);
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
		bg = FEResources.getTexture(right.getTerrain().toString().toLowerCase()
				+ "_bg");

		this.attackQ = attackQ;
		preload();
	}
	
	public void preload(){
		preload(leftFighter);
		preload(rightFighter);
	}
	
	public void preload(FightUnit f){
		AnimationArgs args = f.getAnimArgs();
		if(args.classification.equals("magic")){
			String name = MagicEffect.getTextureName(args);
			if(!PRELOADED_EFFECTS.containsKey(name)){
				System.out.print("PRE");
				PRELOADED_EFFECTS.put(name, FEResources.getTexture(name));
				name = MagicAttack.getBgEffectName(args);
				System.out.print("PRE");
				PRELOADED_EFFECTS.put(name, FEResources.getTexture(name));
			}
		}
		
		for(AttackRecord rec : attackQ){
			boolean crit = rec.animation.contains("Critical");
			for(String effectName : HitEffect.getEffectNames(args, rec)){
				String name = HitEffect.getHitTextureName(effectName, crit);
				if(!PRELOADED_EFFECTS.containsKey(name)){
					System.out.print("PRE");
					PRELOADED_EFFECTS.put(name, FEResources.getTexture(name));
				}
			}
		}
	}
	
	public static Texture getPreload(String name){
		return PRELOADED_EFFECTS.get(name);
	}

	@Override
	public void beginStep() {
		for (Entity e : entities) {
			e.beginStep();
		}
		timer += Game.getDeltaSeconds();
		if (attackQ.size() != 0) {
			processAttackQueue();
		} else if (!done && timer > MIN_TIME) {
			System.out.println(left.name + " HP:" + left.getHp() + " | "
					+ right.name + " HP:" + right.getHp());
			PRELOADED_EFFECTS.clear();
			FEMultiplayer.reportFightResults(this);
			addEntity(new FightOverworldTransition(FEMultiplayer.map, left,
					right));
			done = true;
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
		float dx = Math.signum(cameraOffsetT - cameraOffset) * Game.getDeltaSeconds() * 400;
		cameraOffset += dx;
		if((cameraOffsetT - cameraOffset) * dx < 0){
			cameraOffset = cameraOffsetT;
		}
		float dd = Math.signum(darknessT - darkness);
		darkness += dd;
		if((darknessT - darkness) * dd < 0){
			darkness = darknessT;
		}
	}

	private void processAttackQueue() {
		//TODO Weapon usage
		final AttackRecord rec = attackQ.get(0);
		Unit attacker = FEMultiplayer.getUnit(rec.attacker);
		Unit defender = FEMultiplayer.getUnit(rec.defender);

		FightUnit a = attacker == right ? rightFighter : leftFighter;
		FightUnit d = attacker == right ? leftFighter : rightFighter;
		Healthbar dhp = defender == left ? leftHP : rightHP;
		Healthbar ahp = defender == left ? rightHP : leftHP;

		a.renderDepth = FightStage.UNIT_DEPTH;
		d.renderDepth = FightStage.UNIT_DEPTH + 0.01f;
		
		List<HitEffect> hitEffects = HitEffect.getEffects(a.getAnimArgs(), rec, false);

		if (currentEvent == START) {
			// System.out.println("\n" + rec.attacker.name + "'s turn!");
			// //Debug
			if(attacker == right){
				cameraOffsetT = -cameraOffsetF;
			} else {
				cameraOffsetT = cameraOffsetF;
			}
			ArrayList<String> messages = analyzeAnimation(rec.animation, "(a)", true);
			for (int i = 0; i < messages.size(); i++) {
				addEntity(new SkillIndicator(messages.get(i), attacker == left, i));
			}
			a.setAnimation(rec.animation);
			a.sprite.setSpeed(((AttackAnimation)a.sprite.getCurrentAnimation()).getDefaultSpeed());
			a.sprite.setFrame(0);
			
			d.sprite.setAnimation("ATTACK");
			d.sprite.setFrame(0);
			d.sprite.setSpeed(0);
			
			setCurrentEvent(ATTACKING);
		} else if (currentEvent == ATTACKING) {
			// Let the animation play
		} else if (currentEvent == HIT_EFFECT){
			if(defender == right){
				cameraOffsetT = -cameraOffsetF;
			} else {
				cameraOffsetT = cameraOffsetF;
			}
			
			for (HitEffect h : HitEffect.getEffects(a.getAnimArgs(), rec, true)) {
				addEntity(h);
			}
			if(hitEffects.size() == 0){
				currentEvent = ATTACKED;
			} else {
				currentEvent = HIT_EFFECTED;
			}
			processAddStack();
			
		
		} else if (currentEvent == HIT_EFFECTED){
			//Let anim play
		} else if (currentEvent == ATTACKED) {
			if (rec.damage == 0) {
				// System.out.println("Miss! " + rec.defender.name
				// + " dodged the attack!");
				if (rec.animation.contains("Miss")){
					addEntity(new MissEffect(defender == left));
					d.sprite.setAnimation("DODGE");
					d.sprite.setFrame(0);
					d.sprite.setSpeed(DodgeAnimation.NORMAL_SPEED);
					if(attacker.getWeapon().isMagic()){
						attacker.use(attacker.getWeapon());
					}
				}else{
					addEntity(new NoDamageEffect(defender == left));
					attacker.use(attacker.getWeapon());
				}
				setCurrentEvent(HURTING);
			} else {
				// System.out.println(rec.animation + "! " + rec.defender.name
				// + " took " + rec.damage + " damage!");
				defender.setHp(defender.getHp() - rec.damage);
				attacker.setHp(attacker.getHp() + rec.drain);
				dhp.setHp(dhp.getHp() - rec.damage);
				ahp.setHp(ahp.getHp() + rec.drain, false);
				attacker.use(attacker.getWeapon());
				// battle stats
				if(!defender.getPartyColor().equals(attacker.getPartyColor())) {
					if(rec.damage > 0) {
						defender.getAssisters().add(attacker);
						attacker.addBattleStat("Damage", rec.damage);
						attacker.addBattleStat("Healing", rec.drain);
					}
				} else {
					attacker.addBattleStat("Healing", -rec.damage);
				}
				if(rec.damage > 0) {
					startShaking(hitEffects.get(0).getShakeLength() * 0.05f, hitEffects.get(0).getShakeIntensity());
				}
				
				setCurrentEvent(HURTING);
			}

			ArrayList<String> messages = analyzeAnimation(rec.animation, "(d)", true);
			for (int i = 0; i < messages.size(); i++) {
				addEntity(new SkillIndicator(messages.get(i), attacker != left, i));
			}
		} else if (currentEvent == HURTING) {
			// Try to go to HURTED
			setCurrentEvent(HURTED);
		} else if (currentEvent == HURTED) {
			darknessT= 0;
			if (dhp.getHp() == 0) {
				d.state = FightUnit.FLASHING;
				// battle stats
				attacker.addBattleStat("Kills", 1);
				defender.getAssisters().remove(attacker);
				for(Unit u : defender.getAssisters()) {
					u.addBattleStat("Assists", 1);
				}
				AudioPlayer.playAudio("die", 1, 1);
				currentEvent = DYING;
			} else {
				currentEvent = RETURNING;
			}
		} else if (currentEvent == DYING) {
			// Let animation for dying guy play
		} else if (currentEvent == RETURNING) {
			a.sprite.setSpeed(((AttackAnimation)a.sprite.getCurrentAnimation()).getDefaultSpeed());
			// Let animation play
		} else if (currentEvent == DONE) {
			
			attackQ.remove(0);
			if(attackQ.size() == 0){
				a.sprite.setAnimation("ATTACK");
				a.sprite.setFrame(0);
				a.sprite.setSpeed(0);
			} else {
				currentEvent = START;
			}
		}
	}

	public static ArrayList<String> analyzeAnimation(String animName,
			String suffix, boolean stripNums) {
		ArrayList<String> ans = new ArrayList<String>();
		String[] animations = animName.split(" ");
		for (String anim : animations) {
			if (anim.endsWith(suffix)) {
				anim = anim.substring(0, anim.length() - suffix.length());
			} else {
				continue;
			}
			if (anim.matches(".*\\d") && stripNums) {
				if (anim.endsWith("1")) {
					anim = anim.substring(0, anim.length() - 1);
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
		Renderer.drawRectangle(0, 0, 240, 160, 1, new Color(0,0,0,darkness));
		if (shakeTimer > 0) {
			shakeTimer -= Game.getDeltaSeconds();
			if (prevShakeTimer - shakeTimer > SHAKE_INTERVAL) {
				float factor = Math.min(shakeTimer * 1.2f, 1.0f);
				shakeX *= -factor;
				shakeY *= -factor;
				prevShakeTimer = shakeTimer;
			}
			if (shakeTimer < 0) {
				shakeTimer = 0;
				prevShakeTimer = 0;
				shakeX = 0;
				shakeY = 0;
			}
		}
		
		// Shake
		Renderer.translate((int) shakeX, (int) shakeY);

		SortByRender comparator = new SortByRender();
		PriorityQueue<Entity> renderQueue = new PriorityQueue<Entity>(entities.size()+1, comparator);
		renderQueue.addAll(entities);
		while(!renderQueue.isEmpty()) {
			Entity e = renderQueue.poll();
			Renderer.pushMatrix();
			if(e.renderDepth > HUD_DEPTH && !(e instanceof BackgroundEffect)) {
				Renderer.translate(cameraOffset, 0);
			}
			e.render();
			Renderer.popMatrix();
		}

		// Undo shake translation
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

	private void startShaking(float time, int intensity) {
		shakeTimer = time;
		prevShakeTimer = time;
		float dist = intensity;
		shakeX = -dist;
		shakeY = -dist;
	}

	// Getters Setters

	public void setCurrentEvent(int event) {
		if(event == ATTACKED){

		}
		if (currentEvent == ATTACKING && event == HURTED)
			return;
		if (event == HURTED) {
			if(!hasHitEffects() && leftHP.doneAnimating && rightHP.doneAnimating) {
//				System.out.println(currentEvent + " TO " + event);
				currentEvent = event;
			}
		} else if (event == HURTING || event == currentEvent + 1) {
//			System.out.println(currentEvent + " to " + event);
			currentEvent = event;
		} else {
			throw new IllegalArgumentException("Invalid state transit: "
					+ currentEvent + " to " + event);
		}

	}
	
	public void setDarkness(float dark){
		darknessT = dark;
	}

	public int distanceToHead() {
		return rangeToHeadDistance(range);
	}

	public int getRange() {
		return range;
	}

	public Unit getUnit(int i) {
		return i == 0 ? left : right;
	}

	public boolean isLeft(Unit u) {
		return u == left;
	}

	public static int rangeToHeadDistance(int range) {
		if (range == 1) {
			return 32;
		} else {
			return 54;
		}
	}

	public int getCurrentEvent() {
		return currentEvent;
	}
	
	public void moveCamera(boolean left){
		cameraOffsetT = left? cameraOffsetF: -cameraOffsetF;
	}
	
	public boolean hasHitEffects() {
		for(Entity e : entities) {
			if (e instanceof HitEffect || e instanceof MissEffect || e instanceof NoDamageEffect) {
				// We're not ready yet. Wait for the hiteffects to go away.
//				System.out.println("still have hiteffects "+e.getClass().getCanonicalName());
				return true;
			}
		}
		return false;
	}

}
