package net.fe;

import java.util.HashMap;

import net.fe.overworldStage.objective.Objective;
import net.fe.overworldStage.objective.RoutTheEnemy;

public class Session {
	private HashMap<Byte, Player> players;
	private Objective objective;
	private String map;
	//TODO: Modifiers
	
	public Session() {
		players = new HashMap<Byte, Player>();
		objective = new RoutTheEnemy();
		map = "town";
	}
	
	public void addPlayer(Player p) {
		players.put(p.getID(), p);
	}
	
	public void addPlayer(byte id, Player p) {
		players.put(id, p);
		p.setClientID(id);
	}
	
	public Player removePlayer(Player p) {
		return players.remove(p.getID());
	}
	
	public Player removePlayer(byte id) {
		return players.remove(id);
	}
	
	public Player getPlayer(byte id) {
		return players.get(id);
	}
	
	public int numPlayers() {
		return players.size();
	}
	
	public HashMap<Byte, Player> getAllPlayers() {
		return players;
	}
	
	public Player[] getPlayers() {
		return players.values().toArray(new Player[players.size()]);
	}
	
	public Objective getObjective() {
		return objective;
	}
	
	public void setObjective(Objective objective) {
		this.objective = objective;
	}
	
	public String getMap() {
		return map;
	}
	
	
}
