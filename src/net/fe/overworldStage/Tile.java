package net.fe.overworldStage;

import org.newdawn.slick.Color;

import chu.engine.GriddedEntity;
import chu.engine.Resources;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;

public class Tile extends GriddedEntity{
	private Terrain terrain;
	private int id;
	private Tileset tileset;
	public Tile(int x, int y, Terrain t){
		super(x,y);
		renderDepth = OverworldStage.TILE_DEPTH;
		terrain = t;
		//TODO: Load from a level file
		id = 22;
		tileset = new Tileset(Resources.getTexture("terrain_tiles"), 15, 15);
	}
	
	public void render(){
		tileset.render(x, y, id%18, id/18, renderDepth);
	}
}
