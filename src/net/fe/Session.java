package net.fe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.fe.modifier.Modifier;
import net.fe.overworldStage.objective.Objective;
import net.fe.overworldStage.objective.Rout;
import net.fe.pick.Draft;
import net.fe.pick.PickMode;

/**
 * Contains data on game setup and players.
 * @author Shawn
 *
 */
public class Session implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 696432583909698581L;
	private HashMap<Byte, Player> players;
	private Objective objective;
	private String map;
	private int maxUnits;
	private Set<Modifier> modifiers;
	private PickMode pickMode;
	
	public Session() {
		players = new HashMap<Byte, Player>();
		objective = new Rout();
		modifiers = new HashSet<Modifier>();
		pickMode = new Draft();
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
	
	public void setMap(String map) {
		this.map = map;
	}
	
	public int getMaxUnits() {
		return maxUnits;
	}
	
	public void setMaxUnits(int i) {
		maxUnits = i;
	}

	public Set<Modifier> getModifiers() {
		return modifiers;
	}
	
	public void addModifier(Modifier m) {
		modifiers.add(m);
	}

	public PickMode getPickMode() {
		return pickMode;
	}

	public void setPickMode(PickMode selectedItem) {
		pickMode = selectedItem;
	}
	
	
}
