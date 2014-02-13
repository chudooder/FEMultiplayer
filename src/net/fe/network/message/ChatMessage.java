package net.fe.network.message;

import net.fe.network.Message;

public class ChatMessage extends Message {

	private static final long serialVersionUID = -386437094794678483L;
	public String text;
	public ChatMessage(byte origin, String text) {
		super(origin);
		this.text = text;
	}
	
	public String toString() {
		return super.toString() + text;
	}
	
}
