package net.fe.fightStage;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.anim.Animation;
import chu.engine.anim.Transform;

public class HitEffect extends Entity {
	private boolean left;
	public HitEffect(String name, boolean leftAttacking) {
		super(0, 0);
		left = leftAttacking;
		Texture tex = getHitTexture(name);
		Animation anim = new Animation(tex, 240, 160, 9, 3, 20) {
			@Override
			public void done() {
				destroy();
			}
		};
		sprite.addAnimation("default", anim);
	}
	
	@Override
	public void render() {
		Transform t = new Transform();
		if(left){
			t.flipHorizontal();
		}
		sprite.renderTransformed(FightStage.CENTRAL_AXIS-120, FightStage.FLOOR-104, 0, t);
	}
	
	public static Texture getHitTexture(String name){
		return Resources.getTexture("hit_effect_" + name);
	}
}
