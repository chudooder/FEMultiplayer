package net.fe.overworldStage;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.GriddedEntity;
import chu.engine.anim.Renderer;

public class Tile extends GriddedEntity{
	private Terrain terrain;
	public Tile(int x, int y, Terrain t){
		super(x,y);
		renderDepth = OverworldStage.TILE_DEPTH;
		terrain = t;
	}
	
	public void render(){
		Renderer.drawRectangle(x, y, x+16, y+16, 0, Color.lightGray);
	}
}
