package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.anim.Animation;

public class MissEffect extends Entity {

	private boolean left;
	public MissEffect(boolean left) {
		super(0, 0);
		Animation anim = new Animation(Resources.getTexture("miss"), 38, 26, 20, 5, 0, 0, 30) {
			@Override
			public void update() {
				super.update();
				if(getFrame() == 17)
					setSpeed(100);
			}
			@Override
			public void done() {
				destroy();
			}
		};
		sprite.addAnimation("default", anim);
		this.left = left;
	}
	
	@Override
	public void render() {
		if(left)
			sprite.render(FightStage.CENTRAL_AXIS-60, FightStage.FLOOR-80, 0.0f);
		else
			sprite.render(FightStage.CENTRAL_AXIS+22, FightStage.FLOOR-80, 0.0f);
	}
	
	

}
