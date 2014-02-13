package net.fe.network.message;

import java.util.ArrayList;
import java.util.Arrays;

import net.fe.fightStage.AttackRecord;
import net.fe.network.Message;
import net.fe.unit.UnitIdentifier;

public class CommandMessage extends Message {
	private static final long serialVersionUID = 8131511231319584473L;
	
	public UnitIdentifier unit;
	public int moveX;
	public int moveY;
	public Object[] commands;
	public ArrayList<AttackRecord> attackRecords;
	public CommandMessage(UnitIdentifier unit, 
			int moveX, int moveY, ArrayList<AttackRecord> atk, Object... commands) {
		this.commands = commands;
		this.unit = unit;
		this.moveX = moveX;
		this.moveY = moveY;
		this.attackRecords = atk;
	}
	
	public String toString(){
		if(unit == null){
			return super.toString() + Arrays.toString(commands);
		} else {
			return super.toString() + unit.name + " move (" + moveX + ", " + moveY + "):" + Arrays.toString(commands);
		}
	}

}

