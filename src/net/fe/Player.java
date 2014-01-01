package net.fe;

public class Player {
	private Party party;
	private byte clientID;
	private String nickname;
	
	public Player(byte id) {
		party = new Party();
		clientID = id;
		nickname = "Chu";
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
	
	public String getName() {
		return nickname;
	}
}
