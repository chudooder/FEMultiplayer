package net.fe.network.message;

import net.fe.network.Message;

public class StartPicking extends Message {

	private static final long serialVersionUID = -5415437475972737171L;

	public StartPicking(byte origin) {
		super(origin);
	}

}
