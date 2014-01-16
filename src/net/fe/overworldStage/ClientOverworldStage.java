package net.fe.overworldStage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.fe.Command;
import net.fe.FEMultiplayer;
import net.fe.Party;
import net.fe.Player;
import net.fe.RunesBg;
import net.fe.editor.Level;
import net.fe.editor.SpawnPoint;
import net.fe.network.Chat;
import net.fe.network.message.CommandMessage;
import net.fe.network.message.EndTurn;
import net.fe.overworldStage.context.Idle;
import net.fe.overworldStage.context.WaitForMessages;
import net.fe.unit.Item;
import net.fe.unit.MapAnimation;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;

public class ClientOverworldStage extends OverworldStage {
	private OverworldContext context;
	public final Cursor cursor;
	UnitInfo unitInfo;
	private Menu<?> menu;
	private boolean onControl;
	private float[] repeatTimers;
	private int movX, movY;
	private Unit selectedUnit;
	protected ArrayList<Object> currentCmdString;
	
	public static final float TILE_DEPTH = 0.95f;
	public static final float ZONE_DEPTH = 0.9f;
	public static final float PATH_DEPTH = 0.8f;
	public static final float UNIT_DEPTH = 0.6f;
	public static final float UNIT_MAX_DEPTH = 0.5f;
	public static final float MENU_DEPTH = 0.2f;
	public static final float CURSOR_DEPTH = 0.15f;

	public ClientOverworldStage(String levelName, ArrayList<Player> players) {
		super(levelName, players);
		cursor = new Cursor(2, 2);
		addEntity(cursor);
		unitInfo = new UnitInfo();
		Color c= new Color(FEMultiplayer.getLocalPlayer().getParty().getColor());
		c.r = Math.max(c.r, 0.5f);
		c.g = Math.max(c.g, 0.5f);
		c.b = Math.max(c.b, 0.5f);
		addEntity(new RunesBg(c));
		addEntity(unitInfo);
		addEntity(new TerrainInfo(cursor));
		addEntity(new OverworldChat(this.chat));
		addEntity(new ObjectiveInfo());
		setControl(true);
		if(getCurrentPlayer().equals(FEMultiplayer.getLocalPlayer())) {
			context = new Idle(this, getCurrentPlayer());
			addEntity(new TurnDisplay(true, Party.TEAM_BLUE));
		} else {
			context = new WaitForMessages(this);
			addEntity(new TurnDisplay(false, Party.TEAM_RED));
		}
		repeatTimers = new float[4];
		currentCmdString = new ArrayList<Object>();
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
	
	public void reset(){
		context.cleanUp();
		removeExtraneousEntities();
		new Idle(this, getCurrentPlayer()).startContext();
	}
	
	public void removeExtraneousEntities(Entity... keep){
		List<Entity> keeps = Arrays.asList(keep);
		for(Entity e: entities){
			if(!(e instanceof DoNotDestroy ||
					keeps.contains(e))){
				removeEntity(e);
			}
		}
	}
	
	public void render(){
//		Renderer.scale(2, 2);
		super.render();
	}

	@Override
	public void beginStep() {
		super.beginStep();
		for (Entity e : entities) {
			e.beginStep();
		}
		MapAnimation.updateAll();
		if (onControl) {
			List<KeyboardEvent> keys = Game.getKeys();
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
			for(KeyboardEvent ke : keys) {
				if(ke.state) {
					if(ke.key == Keyboard.KEY_Z) 
						context.onSelect();
					else if (ke.key == Keyboard.KEY_X)
						context.onCancel(); 
				}
			}
		}
		for(int i=0; i<repeatTimers.length; i++) {
			if(repeatTimers[i] > 0) {
				repeatTimers[i] -= Game.getDeltaSeconds();
				if(repeatTimers[i] < 0) repeatTimers[i] = 0;
			}
		}
		processAddStack();
		processRemoveStack();
	}

	@Override
	public void onStep() {
		for (Entity e : entities) {
			e.onStep();
		}
		processAddStack();
		processRemoveStack();
	}

	@Override
	public void endStep() {
		for (Entity e : entities) {
			e.endStep();
		}
		processAddStack();
		processRemoveStack();
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
		FEMultiplayer.getClient().sendMessage(new EndTurn());
		selectedUnit = null;
		removeExtraneousEntities();
	}
	
	@Override
	protected void doEndTurn(int playerID) {
		super.doEndTurn(playerID);
		context.cleanUp();
		// reset assists
		for(Player p : players) {
			for(Unit u : p.getParty()) {
				u.getAssisters().clear();
			}
		}
		send();
		if(FEMultiplayer.getLocalPlayer().getID() == getNextPlayer().getID()){
			context = new Idle(this, FEMultiplayer.getLocalPlayer());
			addEntity(new TurnDisplay(true, Party.TEAM_BLUE));
		} else {
			context = new WaitForMessages(this);
			addEntity(new TurnDisplay(false, Party.TEAM_RED));
		}

	}
	
	private void clearCmdString(){
		selectedUnit = null;
		movX = 0;
		movY = 0;
		currentCmdString.clear();
	}
	
	public void addCmd(Object cmd){
		currentCmdString.add(cmd);
	}
	
	public void addCmd(Object... cmd){
		for(Object o: cmd)
			currentCmdString.add(o);
	}
	
	@Override
	public void processCommands(CommandMessage message) {
		final CommandMessage cmds = (CommandMessage) message;
		boolean execute = true;
		if(cmds.origin == FEMultiplayer.getClient().getID()) {
			execute = false;
		}
		//TODO: command validation
		// Get unit and path
		final Unit unit = (cmds.unit == null ? null : getUnit(cmds.unit));
		// Parse commands
		Command callback = new Command() {
			@Override
			public void execute() {
				if(unit != null) unit.setMoved(true);
			}
		};
		for(int i=0; i<cmds.commands.length; i++) {
			Object obj = cmds.commands[i];
			if(obj.equals("EQUIP") && execute) {
				Unit other = getUnit((UnitIdentifier) cmds.commands[++i]);
				other.equip((Integer) cmds.commands[++i]);
			}
			else if(obj.equals("TRADE")) {
				Unit u1 = getUnit((UnitIdentifier) cmds.commands[++i]);
				int i1 = (Integer)cmds.commands[++i];
				Unit u2 = getUnit((UnitIdentifier) cmds.commands[++i]);
				int i2 = (Integer)cmds.commands[++i];
				//Swap the two items
				if(execute) {
					Item temp = u1.getInventory().get(i1);
					u1.getInventory().set(i1, u2.getInventory().get(i2));
					u2.getInventory().set(i2, temp);
				}
			}
			else if(obj.equals("USE")) {
				final int oHp = unit.getHp();
				final int index = (Integer)cmds.commands[++i];
				if(execute) {
					callback = new Command() {
						public void execute() {
							unit.use(index);
							unit.setMoved(true);
							//TODO Positioning
							addEntity(new Healthbar(
									320, 20 , oHp, 
									unit.getHp(), unit.get("HP")){
								@Override
								public void done() {
									destroy();
								}
							});
						}
					};
				}
			}
			else if(obj.equals("RESCUE")) {
				final Unit rescuee = getUnit((UnitIdentifier) cmds.commands[++i]);
				callback = new Command() {
					public void execute() {
						unit.setMoved(true);
						unit.rescue(rescuee);
					}
				};
			}
			else if(obj.equals("TAKE")) {
				if(execute) {
					Unit other = getUnit((UnitIdentifier) cmds.commands[++i]);
					other.give(unit);
				}
			}
			else if(obj.equals("DROP")) {
				final int dropX = (Integer) cmds.commands[++i];
				final int dropY = (Integer) cmds.commands[++i];
				callback = new Command() {
					public void execute() {
						unit.setMoved(true);
						unit.rescuedUnit().setMoved(true);
						unit.drop(dropX, dropY);
					}
				};
			}
			else if(obj.equals("ATTACK") || obj.equals("HEAL")) {
				final UnitIdentifier other = (UnitIdentifier) cmds.commands[++i];
				callback = new Command() {
					public void execute() {
						unit.setMoved(true);
						FEMultiplayer.goToFightStage(cmds.unit, 
								other, cmds.attackRecords);
					}
				};
			}
		}
		if(execute && unit != null) {
			Path p = grid.getShortestPath(unit, unit.getXCoord()+cmds.moveX, unit.getYCoord()+cmds.moveY);
			grid.move(unit, unit.getXCoord()+cmds.moveX, unit.getYCoord()+cmds.moveY, true);
			unit.move(p, callback);
		} else {
			callback.execute();
		}
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
		UnitIdentifier uid = null;
		if(selectedUnit != null)
			uid = new UnitIdentifier(selectedUnit);
		FEMultiplayer.send(uid, movX, movY, currentCmdString.toArray());
		clearCmdString();
	}
	
	public void loadLevel(String levelName) {
        try {
            InputStream in = ResourceLoader.getResourceAsStream("levels/"+levelName+".lvl");
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
            
            // Add units
            Set<SpawnPoint> spawns = level.spawns;
            for(Player p : players) {
            	Color team = p.getParty().getColor();
    			for(int i=0; i<p.getParty().size(); i++) {
    				SpawnPoint remove = null;
                	for(SpawnPoint sp : spawns) {
                		if(sp.team.equals(team)) {
            				Unit u = p.getParty().getUnit(i);
            				addUnit(u, sp.x, sp.y);
            				remove = sp;
            				break;
                		}
                	}
                	spawns.remove(remove);
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
	
	public Unit getHoveredUnit() {
		return getUnit(cursor.getXCoord(), cursor.getYCoord());
	}
	
	public Terrain getHoveredTerrain() {
		return grid.getTerrain(cursor.getXCoord(), cursor.getYCoord());
	}

	public Unit getSelectedUnit() {
		return selectedUnit;
	}
}
