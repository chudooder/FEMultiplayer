package net.fe.fightStage;

import net.fe.FEResources;
import net.fe.fightStage.anim.AnimationArgs;
import net.fe.fightStage.anim.AttackAnimation;
import net.fe.fightStage.anim.DodgeAnimation;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Transform;

public class FightUnit extends Entity {
	private boolean left;
	private int distanceFromCenter;
	public int state;
	private float alpha;
	private AnimationArgs animArgs;
	private float flashTimer;
	private int flashes;
	
	public static final int ALIVE = 0;
	public static final int FLASHING = 1;
	public static final int FADING = 2;
	
	public FightUnit(AnimationArgs animArgs, FightStage s){
		super(0,0);
		this.left = animArgs.left;
		distanceFromCenter = FightStage.rangeToHeadDistance(animArgs.range);
		state = ALIVE;
		alpha = 1;
		flashTimer = 0;
		flashes = 0;
		this.animArgs = animArgs;
		StringBuilder filename = new StringBuilder();
		filename.append(animArgs.userclass);
		filename.append("_");
		filename.append(animArgs.wepAnimName);
		filename.append("_");
		String base = filename.toString().toLowerCase();
		
		DodgeAnimation dodge = new DodgeAnimation(FEResources.getTextureData(base+"dodge"));
		sprite.addAnimation("DODGE", dodge);
		AttackAnimation crit = AttackAnimation.createAnimation(
				FEResources.getTextureData(base+"critical"), s, animArgs);
		sprite.addAnimation("CRIT", crit);
		AttackAnimation attack = AttackAnimation.createAnimation(
				FEResources.getTextureData(base+"attack"), s, animArgs);
		sprite.addAnimation("ATTACK", attack);
		
		
		renderDepth = FightStage.UNIT_DEPTH;
	}
	
	@Override
	public void beginStep() {
		if(state == FLASHING) {
			flashTimer += Game.getDeltaSeconds();
			if(flashTimer > 0.05f) {
				alpha = 1-alpha;
				flashTimer -= 0.05f;
				flashes++;
				if(flashes >= 12) state = FADING;
			}
		} else if(state == FADING && alpha > 0) {
			alpha -= .75*Game.getDeltaSeconds();
			if(alpha < 0) {
				((FightStage)stage).setCurrentEvent(FightStage.RETURNING);
				alpha = 0;
			}
		}
	}
	
	@Override
	public void render() {
		Transform t = new Transform();
		if(state != ALIVE) {
			t.setColor(new Color(255, 255, 255, alpha));
		}
		String program = (state == FADING ? "lighten" : "default");
		if(left) {
			t.flipHorizontal();
			sprite.render(FightStage.CENTRAL_AXIS - distanceFromCenter, 
					FightStage.FLOOR, renderDepth, t, program);
		} else {
			sprite.render(FightStage.CENTRAL_AXIS + distanceFromCenter,
					FightStage.FLOOR, renderDepth, t, program);
		}
	}
	
	public AnimationArgs getAnimArgs(){
		return animArgs;
	}
	
	public void setAnimation(String animation){
		for(String s: FightStage.analyzeAnimation(animation, "(a)", false)){
			if(sprite.hasAnimation(s)){
				sprite.setAnimation(s);
				return;
			}
		}
		if(animation.contains("Critical")){
			sprite.setAnimation("CRIT");
		} else {
			sprite.setAnimation("ATTACK");
		}
	}

}
