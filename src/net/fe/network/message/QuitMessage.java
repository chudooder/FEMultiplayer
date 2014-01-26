package net.fe.network.message;

import net.fe.network.Message;

public class QuitMessage extends Message {
	private static final long serialVersionUID = 6558427589809693714L;
	public QuitMessage(byte origin) {
		super(origin);
	}

}
