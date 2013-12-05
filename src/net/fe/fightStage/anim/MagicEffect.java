package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.TextureData;
import chu.engine.anim.Animation;
import chu.engine.anim.Transform;

public class MagicEffect extends Entity {
	private AnimationArgs args;

	public MagicEffect(final AnimationArgs args) {
		super(0, 0);
		this.args = args;
		// TODO Get the magic animation
		TextureData data = getTexture(args.unit.getWeapon().name.toLowerCase());
		Animation anim = new Animation(data.texture, data.frameWidth,
				data.frameHeight, data.frames, data.columns, data.offsetX,
				data.offsetY, 20) {
			@Override
			public void done() {
				setFrame(0);
				setSpeed(0);
				((FightStage) stage).setCurrentEvent(FightStage.ATTACKED);
				destroy();
			}
		};
		sprite.addAnimation("default", anim);
		
		renderDepth = FightStage.EFFECT_DEPTH;
	}

	public void render(){
		Transform t = new Transform();
		int offset = FightStage.rangeToHeadDistance(1) - 
				FightStage.rangeToHeadDistance(args.range);
		if (args.left) {
			t.flipHorizontal();
			offset *=-1;
		}
		t.setTranslation(((FightStage)stage).getScrollX(), 0);
		sprite.renderTransformed(FightStage.CENTRAL_AXIS - 120 - offset,				
				FightStage.FLOOR - 104, 0, t);
		
	}

	public static TextureData getTexture(String name) {
		return Resources.getTextureData("magic_effect_" + name);
	}

}
