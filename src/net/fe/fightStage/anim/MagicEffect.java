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
		Texture tex = getHitTexture(args.unit.getWeapon().name.toLowerCase());
		Animation anim = new Animation(tex, 0, 0, 0, 0, 0){
			@Override
			public void done() {
				((FightStage) stage).setCurrentEvent(FightStage.ATTACKED);
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
