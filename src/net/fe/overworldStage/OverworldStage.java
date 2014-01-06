package net.fe.overworldStage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import net.fe.Player;
import net.fe.editor.Level;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.HealCalculator;
import net.fe.network.FEServer;
import net.fe.network.Message;
import net.fe.network.message.CommandMessage;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import chu.engine.Game;
import chu.engine.Stage;

public class OverworldStage extends Stage {
	public Grid grid;
	private HashMap<Integer, Player> players;
	private ArrayList<Player> turnOrder;
	private Player currentPlayer;

	public OverworldStage(String levelName, HashMap<Integer, Player> players) {
		super();
		loadLevel(levelName);
		this.players = players;
		int x = 0;
		//TODO: real spawn locations
		for(Player p : players.values()) {
			for(int i=0; i<p.getParty().size(); i++) {
				Unit u = p.getParty().getUnit(i);
				addUnit(u, x, 0);
				x++;
			}
		}
		turnOrder = new ArrayList<Player>();
		for(Player p : players.values()) {
			turnOrder.add(p);
		}
		currentPlayer = turnOrder.get(0);
		processAddStack();
	}
	
	public Player getPlayer(int i){
		return players.get(i);
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
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
	
	public void loadLevel(String levelName) {
        try {
            FileInputStream in = new FileInputStream(new File("levels/"+levelName+".lvl"));
            ObjectInputStream ois = new ObjectInputStream(in);
            Level level = (Level) ois.readObject();
            grid = new Grid(level.width, level.height, Terrain.NONE);
            for(int i=0; i<level.tiles.length; i++) {
            	for(int j=0; j<level.tiles[0].length; j++) {
            		grid.setTerrain(j, i, Tile.getTerrainFromID(level.tiles[i][j]));
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

	@Override
	public void beginStep() {
		for(Message message : Game.getMessages()) {
			if(message instanceof CommandMessage) {
				processCommands((CommandMessage)message);
			}
		}
	}
	
	public void processCommands(CommandMessage message) {
		CommandMessage cmds = (CommandMessage) message;
		//TODO: command validation
		// After validation, update the unit position
		// Move it instantly since this is the server stage
		Unit unit = getUnit(cmds.unit);
		grid.move(unit, unit.getXCoord()+cmds.moveX, unit.getYCoord()+cmds.moveY, false);
		unit.moved();
		// Parse commands
		
		for(int i=0; i<cmds.commands.length; i++) {
			Object obj = cmds.commands[i];
			if(obj.equals("EQUIP")) {
				Unit other = getUnit((UnitIdentifier) cmds.commands[++i]);
				other.equip((Integer) cmds.commands[++i]);
			}
			else if(obj.equals("ATTACK")) {
				CombatCalculator calc = new CombatCalculator(cmds.unit, (UnitIdentifier) cmds.commands[++i]);
				cmds.attackRecords = calc.getAttackQueue();
			}
			else if(obj.equals("HEAL")) {
				HealCalculator calc = new HealCalculator(cmds.unit, (UnitIdentifier) cmds.commands[++i]);
				cmds.attackRecords = calc.getAttackQueue();
			}
		}
		FEServer.getServer().broadcastMessage(message);
	}

	protected Unit getUnit(UnitIdentifier id) {
		for(Player p: players.values()){
			if(!p.isSpectator() && p.getParty().getColor().equals(id.partyColor)){
				return p.getParty().search(id.name);
			}
		}
		return null;
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub
		
	}
}
