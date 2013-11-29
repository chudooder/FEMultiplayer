package net.fe.fightStage;

import net.fe.unit.Unit;

import org.newdawn.slick.opengl.Texture;

import chu.engine.TextureData;
import chu.engine.anim.Animation;

public class AttackAnimation extends Animation {
	
	private int[] hitframes;
	private int headX;
	private int headY;
	private FightStage stage;
	
	private Unit unit;
	public static final int NORMAL_SPEED = 60;
	
	//TODO You can't have a hit frame on the very last frame
	public AttackAnimation(TextureData data, FightStage stage, Unit u) {
		super(data.texture, data.frameWidth, data.frameHeight, data.rows, data.columns, 0);
		this.hitframes = data.hitframes;
		this.headX = data.headX;
		this.headY = data.headY;
		this.stage = stage;
		this.unit = u;
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
				if(unit.getWeapon().isMagic()){
					stage.addEntity(new MagicEffect(
							unit.getWeapon().name, stage.isLeft(unit)));
				} else {
					stage.setCurrentEvent(FightStage.ATTACKED);
				}
				if(stage.getRange() == 1 || unit.getWeapon().isMagic()){
					setSpeed(0);
				}
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
