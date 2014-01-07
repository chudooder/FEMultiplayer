package net.fe.overworldStage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.Player;
import net.fe.editor.Level;
import net.fe.overworldStage.context.Idle;
import net.fe.unit.MapAnimation;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

import org.lwjgl.input.Keyboard;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class OverworldStage extends Stage {
	public Grid grid;
	private OverworldContext context;
	public final Cursor cursor;
	private Menu<?> menu;

	private Player player;
	private boolean onControl;
	private float[] repeatTimers;
	
	private ArrayList<Object> currentCmdString;
	private int movX, movY;
	private Unit selectedUnit;
	
	public static final float TILE_DEPTH = 1;
	public static final float ZONE_DEPTH = 0.9f;
	public static final float PATH_DEPTH = 0.8f;
	public static final float UNIT_DEPTH = 0.6f;
	public static final float UNIT_MAX_DEPTH = 0.5f;
	public static final float MENU_DEPTH = 0.2f;
	public static final float CURSOR_DEPTH = 0.15f;

	public OverworldStage(String levelName, Player p) {
		super();
		loadLevel(levelName);
		player = p;
		cursor = new Cursor(2, 2);
		addEntity(cursor);
		addEntity(new UnitInfo(cursor));
		addEntity(new TerrainInfo(cursor));
		currentCmdString = new ArrayList<Object>();
		setControl(true);
		context = new Idle(this, p);
		repeatTimers = new float[4];
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
			if(!(e instanceof Tile || e instanceof Unit || e instanceof Cursor || e instanceof UnitInfo ||
					e instanceof TerrainInfo || keeps.contains(e))){
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
		MapAnimation.updateAll();
		if (onControl) {
			HashMap<Integer, Boolean> keys = Game.getKeys();
			if (Keyboard.isKeyDown(Keyboard.KEY_UP) && repeatTimers[0] == 0) {
				context.onUp();
				repeatTimers[0] = 0.15f;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && repeatTimers[1] == 0) {
				context.onDown();
				repeatTimers[1] = 0.15f;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && repeatTimers[2] == 0) {
				context.onLeft();
				repeatTimers[2] = 0.15f;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && repeatTimers[3] == 0) {
				context.onRight();
				repeatTimers[3] = 0.15f;
			}
			if (keys.containsKey(Keyboard.KEY_Z) && keys.get(Keyboard.KEY_Z))
				context.onSelect();
			if (keys.containsKey(Keyboard.KEY_X) && keys.get(Keyboard.KEY_X))
				context.onCancel();
		}
		for(int i=0; i<repeatTimers.length; i++) {
			if(repeatTimers[i] > 0) {
				repeatTimers[i] -= Game.getDeltaSeconds();
				if(repeatTimers[i] < 0) repeatTimers[i] = 0;
			}
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
		send();
		selectedUnit = null;
		removeExtraneousEntities();
		//TODO implement
	}
	
	private void clearCmdString(){
		selectedUnit = null;
		movX = 0;
		movY = 0;
		currentCmdString.clear();
	}
	
	public void addCmd(Object... cmd){
		for(Object o: cmd)
			currentCmdString.add(o);
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
	
	public Unit getHoveredUnit() {
		return getUnit(cursor.getXCoord(), cursor.getYCoord());
	}
	
	public Terrain getHoveredTerrain() {
		return grid.getTerrain(cursor.getXCoord(), cursor.getYCoord());
	}
	
	public void loadLevel(String levelName) {
        try {
            FileInputStream in = new FileInputStream(new File("levels/"+levelName+".lvl"));
            ObjectInputStream ois = new ObjectInputStream(in);
            Level level = (Level) ois.readObject();
            grid = new Grid(level.width, level.height, Terrain.NONE);
            for(int i=0; i<level.tiles.length; i++) {
            	for(int j=0; j<level.tiles[0].length; j++) {
            		Tile tile = new Tile(j, i, level.tiles[i][j]);
            		grid.setTerrain(j, i, tile.getTerrain());
            		addEntity(tile);
            	}
            }
            ois.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
	}
}
