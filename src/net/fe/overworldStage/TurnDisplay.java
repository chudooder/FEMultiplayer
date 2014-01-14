package net.fe.overworldStage;

import net.fe.FEResources;
import net.fe.Party;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Sprite;

public class TurnDisplay extends Entity {
	
	private Sprite text;
	private Sprite flash;
	private float xpos;
	
	private static final float FLY_IN_SPEED = 2000.0f;
	private static final float SLOW_SPEED = 25.0f;

	public TurnDisplay(boolean yourTurn, Color teamColor) {
		super(0, 0);
		xpos = -512;
		renderDepth = 0.0f;
		Texture t, f;
		text = new Sprite();
		flash = new Sprite();
		if(yourTurn) {
			t = FEResources.getTexture("player_phase");
		} else {
			t = FEResources.getTexture("enemy_phase");
		}
		
		if(teamColor == Party.TEAM_BLUE) {
			f = FEResources.getTexture("blue_flash");
		} else {
			f = FEResources.getTexture("red_flash");
		}
		text.addAnimation("default", t);
		flash.addAnimation("default",f);
	}
	
	public void render() {
		if(xpos < -30 || xpos > 0)
			xpos += FLY_IN_SPEED*Game.getDeltaSeconds();
		else
			xpos += SLOW_SPEED*Game.getDeltaSeconds();
		flash.render(-30-xpos, 100, renderDepth);
		text.render(xpos, 100, renderDepth);
	}
	
	public void endStep() {
		if(xpos > 512) {
			destroy();
		}
	}
	
	

}
