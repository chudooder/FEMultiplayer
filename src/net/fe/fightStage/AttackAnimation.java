package net.fe.fightStage;

import net.fe.unit.Unit;

import org.newdawn.slick.opengl.Texture;

import chu.engine.TextureData;
import chu.engine.anim.Animation;

public class AttackAnimation extends Animation {
	
	private int[] hitframes;
	private FightStage stage;
	private int freeze;
	private int loopUntil;
	protected Unit unit;
	public static final int NORMAL_SPEED = 60;
	
	//TODO You can't have a hit frame on the very last frame
	public AttackAnimation(TextureData data, FightStage stage, Unit u) {
		super(data.texture, data.frameWidth, data.frameHeight, data.frames,
				data.columns, data.offsetX, data.offsetY, 0);
		this.hitframes = data.hitframes;
		this.stage = stage;
		this.unit = u;
		this.freeze = data.freeze;
		this.loopUntil = -1;
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
				if(unit.getWeapon().isMagic()){
					stage.addEntity(new MagicEffect(
							unit.getWeapon().name, stage.isLeft(unit)));
				} else {
					stage.setCurrentEvent(FightStage.ATTACKED);
				}
				if(freeze == 0){
					setSpeed(0);
				} else if(freeze > 0) {
					loopUntil = getFrame() + freeze;
				}
			}
		}
		if(getFrame() == loopUntil) {
			setFrame(getFrame()-freeze);
		}
	}
	
	@Override
	public void setSpeed(int speed) {
		super.setSpeed(speed);
		loopUntil = -1;
	}

}
