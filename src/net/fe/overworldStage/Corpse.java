package net.fe.overworldStage;

import net.fe.unit.Unit;
import chu.engine.Entity;
import chu.engine.Game;

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
		sprite.render(x-((ClientOverworldStage) stage).camX, 
				y-((ClientOverworldStage) stage).camY, renderDepth);
	}

}
