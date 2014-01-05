package net.fe;

import java.io.Serializable;

import net.fe.network.message.JoinTeam;

public class Player implements Serializable {
	private static final long serialVersionUID = -7461827659473965623L;
	private Party party;
	private byte clientID;
	private String nickname;
	private int team;
	public boolean ready;
	
	public static final int TEAM_UNASSIGNED = 0;
	public static final int TEAM_SPECTATOR = 1;
	public static final int TEAM_RED = 2;
	public static final int TEAM_BLUE = 3;
	public static final int TEAM_GREEN = 4;
	public static final int TEAM_PURPLE = 5;
	
	public Player(String name, byte id) {
		party = new Party();
		clientID = id;
		nickname = "";
		for(int i=0; i<5; i++) {
			nickname += (char)('A'+(int)(Math.random()*26))+"";
		}
		team = 0;
		ready = false;
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

	public void setClientID(byte id) {
		clientID = id;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team2) {
		team = team2;
	}

	public void joinTeam(int team) {
		FEMultiplayer.getClient().sendMessage(new JoinTeam(clientID, team));		
	}
}
