package net.fe.overworldStage;

import org.newdawn.slick.Color;

import net.fe.unit.Unit;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;

public class Corpse extends Entity {
	
	private float alpha;

	public Corpse(Unit u) {
		super(u.x, u.y);
		alpha = 1.0f;
		sprite.addAnimation("DEFAULT", u.sprite.getAnimation("IDLE"));
		renderDepth = ClientOverworldStage.UNIT_DEPTH;
	}
	
	public void beginStep() {
		alpha -= Game.getDeltaSeconds();
		if(alpha < 0) destroy();
	}
	
	public void render() {
		Transform t = new Transform();
		t.color = new Color(1f, 1f, 1f, alpha);
		sprite.render(x, y, renderDepth, t, new ShaderArgs("greyscale"));
	}

}
