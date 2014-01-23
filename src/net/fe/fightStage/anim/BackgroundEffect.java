package net.fe.fightStage.anim;

import org.newdawn.slick.Color;

import net.fe.FEResources;
import net.fe.fightStage.FightStage;
import chu.engine.Entity;
import chu.engine.AnimationData;
import chu.engine.anim.Animation;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;

public class BackgroundEffect extends Entity {
	private boolean left;
	public BackgroundEffect(String name, boolean left){
		super(0,0);
		renderDepth = FightStage.BG_DEPTH;
		AnimationData data = FEResources.getTextureData("bg_effect_" + name);
		this.left = left;
		sprite.addAnimation("default", new Animation(data.texture, data.frameWidth,
				data.frameHeight, data.frames, data.columns, data.offsetX,
				data.offsetY, data.speed==0.0f?0.05f:data.speed) {
				
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
			t.translateX = 240;
		}
		if(sprite.getCurrentAnimation().getHeight()==1)
			t.setScale(240, 160);
		sprite.render(FightStage.CENTRAL_AXIS - 120,
				FightStage.FLOOR - 104, 0, t);
	}
}
