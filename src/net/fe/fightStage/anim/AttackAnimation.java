package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import net.fe.unit.Unit;

import org.newdawn.slick.opengl.Texture;

import chu.engine.TextureData;
import chu.engine.anim.Animation;

public abstract class AttackAnimation extends Animation {
	
	private int[] hitframes;
	private int headX;
	private int headY;
	protected int loopUntil;
	
	protected AnimationArgs animationArgs;
	protected FightStage stage;
	protected int freeze;
	
	public static final int NORMAL_SPEED = 60;
	
	//TODO You can't have a hit frame on the very last frame
	public AttackAnimation(TextureData data, FightStage stage, AnimationArgs animArgs) {
		super(data.texture, data.frameWidth, data.frameHeight, data.frames,
                data.columns, data.offsetX, data.offsetY, 0);
		this.hitframes = data.hitframes;
		this.stage = stage;
		this.animationArgs = animArgs;
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
				onHit();
//				if(unit.getWeapon().isMagic()){
//					stage.addEntity(new MagicEffect(
//							unit.getWeapon().name, stage.isLeft(unit)));
//				} else {
//					stage.setCurrentEvent(FightStage.ATTACKED);
//				}
//				if(freeze == 0){
//					setSpeed(0);
//				} else if(freeze > 0) {
//					loopUntil = getFrame() + freeze;
//				}
			}
		}
		if(getFrame() == loopUntil) {
			setFrame(getFrame()-freeze);
		}
	}
	
	protected void loopNextFrames(int frames){
		if(frames == 0){
			setSpeed(0);
		} else if (frames > 0){
			loopUntil = getFrame() + frames;
		}
	}

	public int getHeadX() {
		return headX;
	}
	
	public int getHeadY() {
		return headY;
	}
	
	@Override
	public void setSpeed(int speed) {
		super.setSpeed(speed);
		loopUntil = -1;
	}
	
	
	public abstract void onHit();
	
	public static AttackAnimation createAnimation(TextureData data, 
			FightStage stage, AnimationArgs args){
		if(args.classification.equals("normal")){
			return new NormalAttack(data, stage, args);
		} else if (args.classification.equals("ranged")){
			return new ProjectileAttack(data, stage, args);
		} else if (args.classification.equals("magic")){
			return new MagicAttack(data, stage, args);
		}
		return null;
	}

	public int getNextHitFrame() {
		for(int h : hitframes) {
			if(h >= getFrame()) {
				return h;
			}
		}
		return -1;
	}

}
