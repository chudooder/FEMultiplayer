package net.fe.overworldStage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import net.fe.Player;
import net.fe.editor.Level;
import net.fe.unit.Unit;
import chu.engine.Stage;

public class ServerOverworldStage extends Stage {
	public Grid grid;
	private List<Player> players;
	private Player currentPlayer;

	public ServerOverworldStage(String levelName, List<Player> players) {
		super();
		loadLevel(levelName);
		this.players = players;
		int x = 0;
		//TODO: real spawn locations
		for(Player p : players) {
			for(int i=0; i<p.getParty().size(); i++) {
				Unit u = p.getParty().getUnit(i);
				addUnit(u, x, 0);
				x++;
			}
		}
		currentPlayer = players.get(0);
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

	@Override
	public void beginStep() {
		// TODO Auto-generated method stub
		
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
