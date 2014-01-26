package net.fe.network.message;

import java.util.HashMap;

import net.fe.Player;
import net.fe.network.Message;

public class ClientInit extends Message {
	private static final long serialVersionUID = 2559169995718261494L;
	public byte clientID;
	public HashMap<Byte, Player> players;		// Players already in the lobby
	public ClientInit(byte origin, byte clientID, HashMap<Byte, Player> players) {
		super(origin);
		this.clientID = clientID;
		this.players = players;
	}
	
	public String toString() {
		return "INIT "+clientID+" with "+players.size()+" existing players";
	}
}
