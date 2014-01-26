package net.fe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.fe.modifier.MadeInChina;
import net.fe.modifier.Modifier;
import net.fe.overworldStage.objective.Objective;
import net.fe.overworldStage.objective.RoutTheEnemy;

public class Session {
	private HashMap<Byte, Player> players;
	private Objective objective;
	private String map;
	private int maxUnits;
	private Set<Modifier> modifiers;
	
	public Session() {
		players = new HashMap<Byte, Player>();
		objective = new RoutTheEnemy();
		map = "town";
		maxUnits = 8;
		modifiers = new HashSet<Modifier>();
		modifiers.add(new MadeInChina());
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
	
	public HashMap<Byte, Player> getPlayerMap() {
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
	
	public int maxUnits() {
		return maxUnits;
	}

	public Set<Modifier> getModifiers() {
		return modifiers;
	}
	
	
}
