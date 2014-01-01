package net.fe.network.message;

import net.fe.network.Message;

public class JoinLobby extends Message {
	private static final long serialVersionUID = 2466162881020245626L;
	String nick;
	public JoinLobby(int origin, String nick) {
		super(origin);
		this.nick = nick;
	}
	
	public String toString() {
		return "JOIN "+origin+" "+nick;
	}

}
