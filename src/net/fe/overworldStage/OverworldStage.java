package net.fe.overworldStage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.overworldStage.context.Idle;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

import org.lwjgl.input.Keyboard;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Stage;

public class OverworldStage extends Stage {
	public Grid grid;
	private OverworldContext context;
	public final Cursor cursor;
	private Menu<?> menu;

	private Player player;
	private boolean onControl;
	
	private ArrayList<Object> currentCmdString;
	private int movX, movY;
	private Unit selectedUnit;
	
	public static final float TILE_DEPTH = 1;
	public static final float ZONE_DEPTH = 0.8f;
	public static final float PATH_DEPTH = 0.75f;
	public static final float UNIT_DEPTH = 0.5f;
	public static final float MENU_DEPTH = 0.1f;
	public static final float CURSOR_DEPTH = 0.2f;

	public OverworldStage(Grid g, Player p) {
		super();
		grid = g;
		for (int x = 0; x < g.width; x++) {
			for (int y = 0; y < g.height; y++) {
				addEntity(new Tile(x, y, g.getTerrain(x, y)));
			}
		}
		player = p;
		cursor = new Cursor(2, 2);
		addEntity(cursor);
		currentCmdString = new ArrayList<Object>();
		setControl(true);
		context = new Idle(this, p);
	}
	
	public void setMenu(Menu<?> m){
		removeEntity(menu);
		menu = m;
		if(m!=null){
			addEntity(menu);
		}
	}
	
	public Menu<?> getMenu(){
		return menu;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void reset(){
		context.cleanUp();
		removeExtraneousEntities();
		new Idle(this, player).startContext();
	}
	
	public void removeExtraneousEntities(Entity... keep){
		List<Entity> keeps = Arrays.asList(keep);
		for(Entity e: entities){
			if(!(e instanceof Tile || e instanceof Unit || e instanceof Cursor || 
					keeps.contains(e))){
				removeEntity(e);
			}
		}
	}

	public Terrain getTerrain(int x, int y) {
		return grid.getTerrain(x, y);
	}

	public Unit getUnit(int x, int y) {
		return grid.getUnit(x, y);
	}

	public boolean addUnit(Unit u, int x, int y) {
		if (grid.addUnit(u, x, y)) {
			this.addEntity(u);
			return true;
		} else {
			return false;
		}
	}

	public Unit removeUnit(int x, int y) {
		Unit u = grid.removeUnit(x, y);
		if (u != null) {
			this.removeEntity(u);
		}
		return u;
	}
	
	public void removeUnit(Unit u) {
		grid.removeUnit(u.getXCoord(), u.getYCoord());
		this.removeEntity(u);
	}
	
	public void render(){
//		Renderer.scale(2, 2);
		super.render();
	}

	@Override
	public void beginStep() {
		for (Entity e : entities) {
			e.beginStep();
		}
		if (onControl) {
			HashMap<Integer, Boolean> keys = Game.getKeys();
			if (keys.containsKey(Keyboard.KEY_UP) && keys.get(Keyboard.KEY_UP))
				context.onUp();
			if (keys.containsKey(Keyboard.KEY_DOWN)
					&& keys.get(Keyboard.KEY_DOWN))
				context.onDown();
			if (keys.containsKey(Keyboard.KEY_LEFT)
					&& keys.get(Keyboard.KEY_LEFT))
				context.onLeft();
			if (keys.containsKey(Keyboard.KEY_RIGHT)
					&& keys.get(Keyboard.KEY_RIGHT))
				context.onRight();
			if (keys.containsKey(Keyboard.KEY_Z) && keys.get(Keyboard.KEY_Z))
				context.onSelect();
			if (keys.containsKey(Keyboard.KEY_X) && keys.get(Keyboard.KEY_X))
				context.onCancel();
		}
	}

	@Override
	public void onStep() {
		for (Entity e : entities) {
			e.onStep();
		}

	}

	@Override
	public void endStep() {
		for (Entity e : entities) {
			e.endStep();
		}
	}

	void setContext(OverworldContext c) {
		context.cleanUp();
		context = c;
	}
	
	public void setControl(boolean c){
		onControl = c;
	}
	
	public boolean hasControl(){
		return onControl;
	}
	
	public void end(){
		clearCmdString();
		selectedUnit = null;
		removeExtraneousEntities();
		//TODO implement
	}
	
	private void clearCmdString(){
		movX = 0;
		movY = 0;
		currentCmdString.clear();
	}
	
	public void addCmd(Object cmd){
		currentCmdString.add(cmd);
	}
	
	public void setMovX(int x){
		movX = x;
	}
	
	public void setMovY(int y){
		movY = y;
	}
	
	public void setSelectedUnit(Unit u){
		selectedUnit = u;
	}
	
	public void send(){
		FEMultiplayer.send(new UnitIdentifier(selectedUnit), movX, movY, currentCmdString.toArray());
		clearCmdString();
	}
}
