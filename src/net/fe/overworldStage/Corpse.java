package net.fe.overworldStage;

import org.newdawn.slick.Color;

import net.fe.unit.Unit;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;

public class Corpse extends Entity {

	private transient float alpha;

	public Corpse(Unit u) {
		super(u.x, u.y);
		sprite.addAnimation("DYING", u.sprite.getAnimation("IDLE"));
		alpha = 1.0f;
	}
	
	public void beginStep() {
		alpha -= Game.getDeltaSeconds();
		if (alpha < 0) {
			((ClientOverworldStage) stage).setControl(true);
			destroy();
		}
	}
	
	public void render() {
		Transform t = new Transform();
		t.color = new Color(1f, 1f, 1f, alpha);
		sprite.render(x-((ClientOverworldStage) stage).camX, 
				y-((ClientOverworldStage) stage).camY, renderDepth,
				t, new ShaderArgs("greyscale"));
	}

}
