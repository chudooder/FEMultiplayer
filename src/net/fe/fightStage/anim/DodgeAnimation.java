package net.fe.fightStage.anim;

import net.fe.unit.Unit;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;
import chu.engine.TextureData;
import chu.engine.anim.Animation;

public class DodgeAnimation extends Animation {
	
	public static final float DODGE_DURATION = 0.25f;
	public static final int NORMAL_SPEED = 60;
	
	private float dodgeTimer;
	private boolean dodging;

	public DodgeAnimation(TextureData data) {
		super(data.texture, data.frameWidth, data.frameHeight, data.frames,
				data.columns, data.offsetX, data.offsetY,
				DodgeAnimation.NORMAL_SPEED);
		dodgeTimer = 0;
	}
	
	@Override
	public void update() {
		super.update();
		if(dodging) {
			dodgeTimer += Game.getDeltaSeconds();
			if(dodgeTimer > DODGE_DURATION) {
				dodging = false;
				setSpeed(-DodgeAnimation.NORMAL_SPEED);
			}
		}
	}
	
	@Override
	public void done() {
		if(speed > 0) {
			dodging = true;
			setSpeed(0);
			setFrame(getLength()-1);
		} else if (speed < 0) {
			getSprite().setAnimation("ATTACK");
			getSprite().setSpeed(0);
			dodging = false;
		}
	}
	

}
