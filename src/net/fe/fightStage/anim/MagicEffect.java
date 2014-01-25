package net.fe.fightStage.anim;

import java.util.HashMap;

import net.fe.FEResources;
import net.fe.fightStage.FightStage;
import chu.engine.Entity;
import chu.engine.AnimationData;
import chu.engine.anim.Animation;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Transform;

public class MagicEffect extends Entity {
	private AnimationArgs args;

	public MagicEffect(final AnimationArgs args) {
		super(0, 0);
		this.args = args;
		final AnimationData data = getTexture(args.unit.getWeapon().name.toLowerCase());
		Animation anim = new Animation(data.texture, data.frameWidth,
				data.frameHeight, data.frames, data.columns, data.offsetX,
				data.offsetY, data.speed==0.0f?0.05f:data.speed) {
			HashMap<Integer, String> soundMap = data.soundMap;
			int prevFrame = -1;
			public void update() {
				super.update();
				if(soundMap.get(getFrame()) != null && prevFrame != getFrame()) {
					prevFrame = getFrame();
					AudioPlayer.playAudio(soundMap.get(getFrame()), 1, 1);
				}
			}
			@Override
			public void done() {
				setFrame(0);
				setSpeed(0);
				((FightStage) stage).setCurrentEvent(FightStage.HIT_EFFECT);
				((FightStage) stage).moveCamera(args.left);
				destroy();
			}
		};
		sprite.addAnimation("default", anim);
		
		renderDepth = FightStage.EFFECT_DEPTH;
	}

	public void render(){
		Transform t = new Transform();
		int offset = FightStage.rangeToHeadDistance(args.range);
		if (args.left) {
			t.flipHorizontal();
			offset *=-1;
		}
		sprite.render(FightStage.CENTRAL_AXIS + offset,				
				FightStage.FLOOR, 0, t);
		
	}

	public static AnimationData getTexture(String name) {
		return FEResources.getTextureData("magic_effect_" + name);
	}

}
