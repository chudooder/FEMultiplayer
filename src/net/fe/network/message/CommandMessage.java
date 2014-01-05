package net.fe.network.message;

import java.util.ArrayList;

import net.fe.network.Message;
import net.fe.unit.UnitIdentifier;

public class CommandMessage extends Message {
	private static final long serialVersionUID = 8131511231319584473L;
	
	public UnitIdentifier unit;
	public int moveX;
	public int moveY;
	public Object[] commands;
	public CommandMessage(UnitIdentifier unit, 
			int moveX, int moveY, Object... commands) {
		this.commands = commands;
		this.unit = unit;
		this.moveX = moveX;
		this.moveY = moveY;
	}

}
