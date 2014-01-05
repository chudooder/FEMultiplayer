package net.fe;

public class Player {
	private Party party;
	public final String name;
	private byte clientID;
	
	public Player(String name, byte id) {
		party = new Party();
		clientID = id;
		this.name = name;
	}
	
	public boolean isSpectator() {
		return party.size() == 0;
	}
	
	public Party getParty() {
		return party;
	}
	
	public byte getID() {
		return clientID;
	}
}
