package net.fe.fightStage;

import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.anim.Animation;
import chu.engine.anim.Transform;

public class FightUnit extends Entity {
	private boolean left;
	public FightUnit(boolean left) {
		super(0, 0);
		this.left = left;
	}
	
	@Override
	public void render() {
		Transform t = new Transform();
		if(left) t.flipHorizontal();
		if(left) {
			sprite.renderTransformed(x, y, renderDepth, t);
		} else {
			sprite.renderTransformed(x+60, y, renderDepth, t);
		}
	}

}
