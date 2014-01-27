package net.fe.network.message;

import net.fe.network.Message;

public class StartGame extends Message {
	private static final long serialVersionUID = 996855764072238544L;
	public StartGame(byte origin) {
		super(origin);
	}
}
