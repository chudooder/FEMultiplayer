package net.fe.network.message;

import net.fe.network.Message;

public class ReadyMessage extends Message {

	private static final long serialVersionUID = 2430536015559627885L;

	public ReadyMessage(byte origin) {
		super(origin);
	}

	public ReadyMessage() {
		super();
	}

}
