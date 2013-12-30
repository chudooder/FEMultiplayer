package net.fe.fightStage.anim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.fe.FEResources;
import net.fe.fightStage.FightStage;
import net.fe.unit.Weapon;
import chu.engine.Entity;
import chu.engine.AnimationData;
import chu.engine.anim.Animation;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Transform;

public class HitEffect extends Entity {
	private boolean left;

	public HitEffect(String name, boolean leftAttacking) {
		super(0, 0);
		left = leftAttacking;
		final AnimationData data = getHitTexture(name);
		Animation anim = new Animation(data.texture, data.frameWidth,
				data.frameHeight, data.frames, data.columns, data.offsetX,
				data.offsetY, 0.02f) {
			HashMap<Integer, String> soundMap = data.soundMap;
			{
				if(soundMap.get(0) != null) {
					System.out.println(soundMap.get(0));
					AudioPlayer.playAudio(soundMap.get(0), 1, 1);
				}
			}
			public void update() {
				int prevFrame = getFrame();
				super.update();
				if(prevFrame != getFrame()) {
					if(soundMap.get(getFrame()) != null) {
						AudioPlayer.playAudio(soundMap.get(getFrame()), 1, 1);
					}
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

	public static AnimationData getHitTexture(String name) {
		return FEResources.getTextureData("hit_effect_" + name);
	}

	public static List<HitEffect> getEffects(AnimationArgs animArgs,
			String animation) {
		List<HitEffect> effects = new ArrayList<HitEffect>();

		if (animArgs.unit.getWeapon().isMagic()) {
			effects.add(new HitEffect(animArgs.unit.getWeapon().name
					.toLowerCase(), animArgs.left));
		}
		if (animArgs.unit.getWeapon().type == Weapon.Type.STAFF) {
			effects.add(new HitEffect("heal", animArgs.left));
		}
		if (effects.size() == 0 && !animation.equals("Miss")) { // We have
																// nothing.
			effects.add(new HitEffect(
					animation.contains("Critical") ? "critical" : "attack",
					animArgs.left));
		}

		// TODO Skill Hit Effects

		return effects;
	}
}
