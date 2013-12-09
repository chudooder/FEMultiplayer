package net.fe.overworldStage;
import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.GriddedEntity;
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
		if(time < .75f && ((OverworldStage) stage).hasControl())
			Renderer.drawLine(x+2, y+15, x+14, y+15, 1, 0, Color.black, Color.black);
	}

	public void setXCoord(int xcoord) {
		this.xcoord = xcoord;
	}

	public void setYCoord(int ycoord) {
		this.ycoord = ycoord;
	}

}
