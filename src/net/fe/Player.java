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
	public static final int TEAM_PLAYERS = 2;
	
	public Player(String name, byte id) {
		party = new Party();
		clientID = id;
		nickname = name;
		team = 0;
		ready = false;
	}
	
	public boolean isSpectator() {
		return team == TEAM_SPECTATOR;
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
	
	public boolean equals(Player p) {
		return p.clientID == clientID;
	}

	public void setName(String nickname) {
		this.nickname = nickname;
	}
}
