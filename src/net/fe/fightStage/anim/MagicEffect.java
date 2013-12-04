package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.anim.Animation;

public class MagicEffect extends Entity {

	public MagicEffect(final AnimationArgs args) {
		super(0, 0);
		//TODO Get the magic animation
		Texture tex = getHitTexture(args.unit.getWeapon().name);
		Animation anim = new Animation(tex, 0, 0, 0, 0, 0){
			@Override
			public void done() {
				//TODO Add projectile coords
				stage.addEntity(new Projectile(
						args.unit.getWeapon().name,
						FightStage.FLOOR - 50f, 
						(FightStage) stage,
						args.left,
						true));
				destroy();
			}
		};
		sprite.addAnimation("default", anim);
		renderDepth = FightStage.EFFECT_DEPTH;
	}
	
	public void render(){
		//TODO Implement
	}
	
	public static Texture getHitTexture(String name){
		return Resources.getTexture("magic_effect_" + name);
	}

}
