package net.fe.network.message;

import net.fe.Session;
import net.fe.network.Message;

public class SessionUpdate extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1593840263817973024L;
	public Session session;
	
	public SessionUpdate(Session s) {
		session = s;
	}

}
