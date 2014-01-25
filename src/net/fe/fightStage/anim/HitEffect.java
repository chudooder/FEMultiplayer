package net.fe.fightStage.anim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.fe.FEResources;
import net.fe.fightStage.AttackRecord;
import net.fe.fightStage.FightStage;
import net.fe.unit.Weapon;
import chu.engine.Entity;
import chu.engine.AnimationData;
import chu.engine.anim.Animation;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Transform;

public class HitEffect extends Entity {
	private boolean left;
	private int shakeLength;
	private int shakeIntensity;

	public HitEffect(String name, boolean leftAttacking) {
		super(0, 0);
		left = leftAttacking;
		final AnimationData data = getHitTexture(name);
		if(data.shakeIntensity > 0){
			shakeIntensity = data.shakeIntensity;
		} else {
			shakeIntensity = 5;
		}
		if(data.shakeFrames > 0){
			shakeLength = data.shakeFrames;
		} else if(data.hitframes.length > 0){
			shakeLength = data.frames - data.hitframes[0];
		} else {
			shakeLength = data.frames;
		}
		Animation anim = new Animation(data.texture, data.frameWidth,
				data.frameHeight, data.frames, data.columns, data.offsetX,
				data.offsetY, data.speed==0.0f?0.05f:data.speed) {
			HashMap<Integer, String> soundMap = data.soundMap;
			int hitframe;
			int prevFrame;
			{
				if(data.hitframes.length != 0){
					hitframe = data.hitframes[0];
				} else {
					hitframe = 0;
				}
				prevFrame = -1;
			}
			public void update() {
				super.update();
				if(soundMap.get(getFrame()) != null && prevFrame != getFrame()) {
					prevFrame = getFrame();
					AudioPlayer.playAudio(soundMap.get(getFrame()), 1, 1);
				}
				if(getFrame()>hitframe && hitframe >= 0){
					hitframe = -1; //Some big number.
					((FightStage) stage).setCurrentEvent(FightStage.ATTACKED);
				}
			}
			@Override
			public void done() {
				destroy();
			}
		};
		sprite.addAnimation("default", anim);
		renderDepth = FightStage.EFFECT_DEPTH;
	}

	@Override
	public void render() {
		Transform t = new Transform();
		int offset = - ((FightStage) stage).distanceToHead();
		if (left) {
			t.flipHorizontal();
			offset *= -1;
		}
		sprite.render(FightStage.CENTRAL_AXIS + offset,
				FightStage.FLOOR, renderDepth, t);
	}
	
	public int getShakeLength(){
		return shakeLength;
	}

	public static AnimationData getHitTexture(String name) {
		return FEResources.getTextureData("hit_effect_" + name);
	}

	public static List<HitEffect> getEffects(AnimationArgs animArgs,
			AttackRecord rec) {
		List<HitEffect> effects = new ArrayList<HitEffect>();

		if (animArgs.unit.getWeapon().isMagic()) {
			effects.add(new HitEffect(animArgs.unit.getWeapon().name
					.toLowerCase(), animArgs.left));
		}
		if (animArgs.unit.getWeapon().type == Weapon.Type.STAFF) {
			effects.add(new HitEffect("heal", animArgs.left));
		}
		if (effects.size() == 0 && rec.damage != 0) { // We have
																// nothing.
			effects.add(new HitEffect(
					rec.animation.contains("Critical") ? "critical" : "attack",
					animArgs.left));
		}

		// TODO Skill Hit Effects

		return effects;
	}

	public int getShakeIntensity() {
		return shakeIntensity;
	}
	
	
}
