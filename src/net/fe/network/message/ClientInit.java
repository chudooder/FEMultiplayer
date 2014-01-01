package net.fe.network.message;

import net.fe.network.Message;

public class ClientInit extends Message {
	private static final long serialVersionUID = 2559169995718261494L;
	public byte clientID;
	public ClientInit(int origin, byte clientID) {
		super(origin);
		this.clientID = clientID;
	}
	
	public String toString() {
		return "INIT "+clientID;
	}
}
