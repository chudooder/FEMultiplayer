package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import net.fe.unit.Weapon;
import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.TextureData;
import chu.engine.anim.Animation;
import chu.engine.anim.Transform;

public class HitEffect extends Entity {
	private boolean left;

	public HitEffect(AnimationArgs animArgs, boolean crit) {
		super(0, 0);
		left = animArgs.left;
		String name = crit? "critical" : "attack";
		if(animArgs.unit.getWeapon().isMagic()){
			name = animArgs.wepAnimName;
		} else if (animArgs.unit.getWeapon().type == Weapon.Type.STAFF){
			name = "heal";
		}
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
		int offset = FightStage.rangeToHeadDistance(1) - 
				((FightStage) stage).distanceToHead();
		if (left) {
			t.flipHorizontal();
			offset*=-1;
		}
		sprite.renderTransformed(FightStage.CENTRAL_AXIS - 120 + offset,
				FightStage.FLOOR - 104, 0, t);
	}

	public static TextureData getHitTexture(String name) {
		return Resources.getTextureData("hit_effect_" + name);
	}
}
