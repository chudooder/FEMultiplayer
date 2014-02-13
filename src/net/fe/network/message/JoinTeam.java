package net.fe.network.message;

import net.fe.network.Message;

public class JoinTeam extends Message {
	private static final long serialVersionUID = -2805120675582622842L;
	public int team;
	public JoinTeam(byte origin, int team) {
		super(origin);
		this.team = team;
	}
	
	public String toString() {
		return super.toString()+team;
	}
}
