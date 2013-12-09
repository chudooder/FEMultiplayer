package net.fe.overworldStage;
import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.GriddedEntity;
import chu.engine.Resources;
import chu.engine.anim.Renderer;

public class Cursor extends GriddedEntity{
	private float time;
	public Cursor(int xx, int yy) {
		super(xx, yy);
	}
	
	public void onStep(){
		time+= Game.getDeltaSeconds();
		if(time >=1.5){
			time -=1.5;
		}
		
	}
	
	public void render(){
		if(((OverworldStage) stage).hasControl())
			Renderer.render(Resources.getTexture("cursor"),
					0, 0, 1, 1, x, y, x + 16, y + 16, 0);
	}

	public void setXCoord(int xcoord) {
		this.xcoord = xcoord;
	}

	public void setYCoord(int ycoord) {
		this.ycoord = ycoord;
	}

}
