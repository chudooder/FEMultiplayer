package net.fe;

public class Player {
	private Party party;
	private byte clientID;
	
	public Player(byte id) {
		party = new Party();
		clientID = id;
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
