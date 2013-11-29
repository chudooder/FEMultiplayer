package net.fe.fightStage;

import org.newdawn.slick.opengl.Texture;

import chu.engine.TextureData;
import chu.engine.anim.Animation;

public class AttackAnimation extends Animation {
	
	private int[] hitframes;
	private int headX;
	private int headY;
	private FightStage stage;
	
	private boolean freeze;
	
	public static final int NORMAL_SPEED = 60;

	public AttackAnimation(Texture t, int width, int height, int frames,
			int columns, int speed, int[] hitframes, FightStage stage, boolean freeze) {
		super(t, width, height, frames, columns, speed);
		this.hitframes = hitframes;
		this.stage = stage;
		this.freeze = freeze;
	}
	
	public AttackAnimation(TextureData data, FightStage stage, boolean freeze) {
		super(data.texture, data.frameWidth, data.frameHeight, data.rows, data.columns, 0);
		this.hitframes = data.hitframes;
		this.headX = data.headX;
		this.headY = data.headY;
		this.stage = stage;
		this.freeze = freeze;
	}
	
	@Override
	public void done() {
		((FightStage)stage).setCurrentEvent(FightStage.DONE);
		setFrame(0);
		setSpeed(0);
	}
	
	@Override
	public void update() {
		FightStage fs = ((FightStage)stage);
		int prevFrame = getFrame();
		super.update();
		for(int i : hitframes) {
			if(prevFrame != getFrame() && getFrame() == i) {
				((FightStage)stage).setCurrentEvent(FightStage.ATTACKED);
				setSpeed(0);
			}
		}
	}

	public int getHeadX() {
		return headX;
	}
	
	public int getHeadY() {
		return headY;
	}

}
