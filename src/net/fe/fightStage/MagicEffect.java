package net.fe.fightStage;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.anim.Animation;

public class MagicEffect extends Entity {

	public MagicEffect(final String name, final boolean left) {
		super(0, 0);
		//TODO Get the magic animation
		Texture tex = getHitTexture(name);
		Animation anim = new Animation(tex, 0, 0, 0, 0, 0){
			@Override
			public void done() {
				//TODO Add projectile coords
				stage.addEntity(new Projectile(
						name,FightStage.FLOOR - 50f,(FightStage) stage,left,true));
				destroy();
			}
		};
		sprite.addAnimation("default", anim);
		
	}
	
	public void render(){
		//TODO Implement
	}
	
	public static Texture getHitTexture(String name){
		return Resources.getTexture("magic_effect_" + name);
	}

}
