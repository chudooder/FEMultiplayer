package net.fe.fightStage.anim;

import java.util.ArrayList;
import java.util.List;

import net.fe.fightStage.FightStage;
import net.fe.unit.Weapon;
import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.TextureData;
import chu.engine.anim.Animation;
import chu.engine.anim.Transform;

public class HitEffect extends Entity {
	private boolean left;

	public HitEffect(String name, boolean leftAttacking) {
		super(0, 0);
		left = leftAttacking;
		TextureData data = getHitTexture(name);
		Animation anim = new Animation(data.texture, data.frameWidth,
				data.frameHeight, data.frames, data.columns, data.offsetX,
				data.offsetY, 20) {
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
		int offset = FightStage.rangeToHeadDistance(1)
				- ((FightStage) stage).distanceToHead();
		if (left) {
			t.flipHorizontal();
			offset *= -1;
		}
		sprite.renderTransformed(FightStage.CENTRAL_AXIS - 120 + offset,
				FightStage.FLOOR - 104, 0, t);
	}

	public static TextureData getHitTexture(String name) {
		return Resources.getTextureData("hit_effect_" + name);
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
