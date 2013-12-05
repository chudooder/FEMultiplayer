package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.TextureData;
import chu.engine.anim.Animation;
import chu.engine.anim.Transform;

public class BackgroundEffect extends Entity {
	private boolean left;
	public BackgroundEffect(String name, boolean left){
		super(0,0);
		renderDepth = FightStage.BG_DEPTH;
		TextureData data = Resources.getTextureData("bg_effect_" + name);
		sprite.addAnimation("default", new Animation(data.texture, data.frameWidth,
				data.frameHeight, data.frames, data.columns, data.offsetX,
				data.offsetY, 20) {
				
			@Override
			public void done() {
				destroy();
			}
		});
	}
	
	public void render(){
		Transform t = new Transform();
		if(left){
			t.flipHorizontal();
		}
		sprite.renderTransformed(FightStage.CENTRAL_AXIS - 120,
				FightStage.FLOOR - 104, 0, t);
	}
}
