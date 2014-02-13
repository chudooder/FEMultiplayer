package net.fe.network.message;

import net.fe.Session;
import net.fe.network.Message;

public class ClientInit extends Message {
	private static final long serialVersionUID = 2559169995718261494L;
	public byte clientID;
	public Session session;	// Session data
	public ClientInit(byte origin, byte clientID, Session s) {
		super(origin);
		this.clientID = clientID;
		this.session = s;
	}
	
	public String toString(){
		return super.toString() + "clientID = " + clientID;
	}
}
