package net.fe.overworldStage;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import net.fe.overworldStage.context.Idle;
import net.fe.unit.Unit;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Stage;

public class OverworldStage extends Stage{
	public Grid grid;
	private OverworldContext context;
	public Cursor cursor;

	public static final int TILE_WIDTH = 32;
	
	public OverworldStage(Grid g) {
		super();	
		grid = g;
		for(int x = 0; x < g.width; x++){
			for(int y = 0; y < g.height; y++){
				addEntity(new Tile(x, y, g.getTerrain(x, y)));
			}
		}
		context = new Idle(this);
		cursor = new Cursor(0,0);
		addEntity(cursor);
	}
	
	public Terrain getTerrain(int x, int y){
		return grid.getTerrain(x, y);
	}
	
	public Unit getUnit(int x, int y){
		return grid.getUnit(x, y);
	}
	
	public boolean addUnit(Unit u, int x, int y) {
		if(grid.addUnit(u, x, y)){
			this.addEntity(u);
			return true;
		} else {
			return false;
		}
	}
	
	public Unit removeUnit(int x, int y) {
		Unit u = grid.removeUnit(x, y);
		if(u!=null){
			this.removeEntity(u);
		}
		return u;
	}

	@Override
	public void beginStep() {
		for(Entity e: entities){
			e.beginStep();
		}
		HashMap<Integer, Boolean> keys = Game.getKeys();
		if(keys.containsKey(Keyboard.KEY_UP) && keys.get(Keyboard.KEY_UP)) context.onUp();
		if(keys.containsKey(Keyboard.KEY_DOWN) && keys.get(Keyboard.KEY_DOWN)) context.onDown();
		if(keys.containsKey(Keyboard.KEY_LEFT) && keys.get(Keyboard.KEY_LEFT)) context.onLeft();
		if(keys.containsKey(Keyboard.KEY_RIGHT) && keys.get(Keyboard.KEY_RIGHT)) context.onRight();
		if(keys.containsKey(Keyboard.KEY_Z)&& keys.get(Keyboard.KEY_Z)) context.onSelect();
	}

	@Override
	public void onStep() {
		for(Entity e: entities){
			e.onStep();
		}
		
		
	}

	@Override
	public void endStep() {
		for(Entity e: entities){
			e.endStep();
		}
	}
	
	
	
}
