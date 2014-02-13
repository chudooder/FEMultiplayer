package net.fe.network.message;

import net.fe.Player;
import net.fe.network.Message;

public class JoinLobby extends Message {
	private static final long serialVersionUID = 2466162881020245626L;
	public Player player;
	public JoinLobby(byte origin, Player player) {
		super(origin);
		this.player = player;
	}
	
	public String toString() {
		return super.toString()+player;
	}

}
