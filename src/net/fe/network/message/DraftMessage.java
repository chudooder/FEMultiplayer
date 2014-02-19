package net.fe.network.message;

import net.fe.network.Message;

public class DraftMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2354614661871074893L;

	public String[] unitNames;
	
	public DraftMessage(String[] unitNames) {
		super();
		this.unitNames = unitNames;
	}
}
