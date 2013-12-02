package net.fe.fightStage;

import net.fe.fightStage.anim.AnimationArgs;
import net.fe.fightStage.anim.AttackAnimation;
import net.fe.fightStage.anim.DodgeAnimation;
import net.fe.fightStage.anim.NormalAttack;
import net.fe.unit.Unit;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Resources;
import chu.engine.anim.Animation;
import chu.engine.anim.Transform;

public class FightUnit extends Entity {
	private boolean left;
	private int distanceFromCenter;
	public boolean dying;
	private float alpha;
	
	public FightUnit(AnimationArgs animArgs, FightStage s){
		super(0,0);
		this.left = animArgs.left;
		distanceFromCenter = FightStage.rangeToHeadDistance(animArgs.range);
		dying = false;
		alpha = 1;
		StringBuilder filename = new StringBuilder();
		filename.append(animArgs.userclass);
		filename.append("_");
		filename.append(animArgs.wepAnimName);
		filename.append("_");
		String base = filename.toString().toLowerCase();
		
		DodgeAnimation dodge = new DodgeAnimation(Resources.getTextureData(base+"dodge"));
		sprite.addAnimation("DODGE", dodge);
		AttackAnimation attack = new NormalAttack(
				Resources.getTextureData(base+"attack"), s, animArgs);
		sprite.addAnimation("ATTACK", attack);
		AttackAnimation crit = new NormalAttack(
				Resources.getTextureData(base+"critical"), s, animArgs);
		sprite.addAnimation("CRIT", crit);
	}
	
	@Override
	public void beginStep() {
		if(dying && alpha > 0) {
			alpha -= .75*Game.getDeltaSeconds();
			if(alpha < 0) alpha = 0;
		}
	}
	
	@Override
	public void render() {
		Transform t = new Transform();
		t.setColor(new Color(255, 255, 255, alpha));
//		int y1 = FightStage.FLOOR - sprite.getCurrentAnimation().getOffsetY();
		if(left) {
			t.flipHorizontal();
			sprite.renderTransformed(FightStage.CENTRAL_AXIS - distanceFromCenter
					- (sprite.getCurrentAnimation().getWidth()) + 2*sprite.getCurrentAnimation().getOffsetX(), 
					FightStage.FLOOR, renderDepth, t);
		} else {
			sprite.renderTransformed(FightStage.CENTRAL_AXIS + distanceFromCenter,
					FightStage.FLOOR, renderDepth, t);
		}
	}

}
