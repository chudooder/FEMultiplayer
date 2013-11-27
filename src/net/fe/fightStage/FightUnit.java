package net.fe.fightStage;

import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.anim.Animation;
import chu.engine.anim.Transform;

public class FightUnit extends Entity {
	private boolean left;
	private int distanceFromCenter;
	public FightUnit(boolean left, int range) {
		super(0, 0);
		this.left = left;
		distanceFromCenter = range;
	}
	
	@Override
	public void render() {
		Transform t = new Transform();
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
