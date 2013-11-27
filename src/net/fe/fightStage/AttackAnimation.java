package net.fe.fightStage;

import org.newdawn.slick.opengl.Texture;

import chu.engine.TextureData;
import chu.engine.anim.Animation;

public class AttackAnimation extends Animation {
	
	private int[] hitframes;
	private FightStage stage;

	public AttackAnimation(Texture t, int width, int height, int rows,
			int columns, int speed, int[] hitframes, FightStage stage) {
		super(t, width, height, rows, columns, speed);
		this.hitframes = hitframes;
		this.stage = stage;
	}
	
	public AttackAnimation(TextureData data, FightStage stage) {
		super(data.texture, data.frameWidth, data.frameHeight, data.rows, data.columns, 0);
		this.hitframes = data.hitframes;
		this.stage = stage;
	}
	
	@Override
	public void done() {
		((FightStage)stage).setCurrentEvent(FightStage.DONE);
		setFrame(0);
		setSpeed(0);
	}
	
	@Override
	public void update() {
		int prevFrame = getFrame();
		super.update();
		for(int i : hitframes) {
			if(prevFrame != getFrame() && getFrame() == i) {
				((FightStage)stage).setCurrentEvent(FightStage.ATTACKED);
			}
		}
	}

}
