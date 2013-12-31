package net.fe.overworldStage;

import net.fe.FEResources;

import org.newdawn.slick.Color;

import chu.engine.GriddedEntity;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;

public class Tile extends GriddedEntity{
	private Terrain terrain;
	private int id;
	private static transient Tileset tileset;
	static {
		tileset = new Tileset(FEResources.getTexture("terrain_tiles"), 16, 16);
	}
	public Tile(int x, int y, int id) {
		super(x,y);
		renderDepth = OverworldStage.TILE_DEPTH;
		this.id = id;
		terrain = Terrain.PLAIN;		
	}
	
	public void render(){
		tileset.render(x, y, id%25, id/25, renderDepth);
	}
}
