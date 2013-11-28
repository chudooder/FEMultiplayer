package net.fe.fightStage;

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
	public FightUnit(boolean left, int range) {
		super(0, 0);
		this.left = left;
		distanceFromCenter = range;
		dying = false;
		alpha = 1;
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
		int y1 = FightStage.FLOOR - sprite.getCurrentAnimation().getHeight();
		if(left) {
			t.flipHorizontal();
			sprite.renderTransformed(FightStage.CENTRAL_AXIS + distanceFromCenter
					- sprite.getCurrentAnimation().getWidth(), 
					y1, renderDepth, t);
		} else {
			sprite.renderTransformed(FightStage.CENTRAL_AXIS - distanceFromCenter,
					y1, renderDepth, t);
		}
	}

}
