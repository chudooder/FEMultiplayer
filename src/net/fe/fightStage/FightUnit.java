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
		Animation attack = new Animation(Resources.getTexture("ROY_ATTACK"), 142, 102, 4, 24, 0) {
			@Override
			public void done() {
				((FightStage)stage).setCurrentEvent(FightStage.DONE);
				sprite.setFrame(0);
				sprite.setSpeed(0);
			}
			
			@Override
			public void update() {
				int prevFrame = getFrame();
				super.update();
				if(prevFrame != getFrame() && getFrame() == 47) {
					((FightStage)stage).setCurrentEvent(FightStage.ATTACKED);
				}
			}
		};
		sprite.addAnimation("ATTACK", attack);
	}
	
	@Override
	public void render() {
		Transform t = new Transform();
		if(left) t.flipHorizontal();
		if(!left) {
			sprite.renderTransformed(x+60, y, renderDepth, t);
		} else {
			sprite.renderTransformed(x, y, renderDepth, t);
		}
	}

}
