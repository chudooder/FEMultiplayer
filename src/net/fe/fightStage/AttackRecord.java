package net.fe.fightStage;

import java.io.Serializable;

import net.fe.unit.UnitIdentifier;

public class AttackRecord implements Serializable {
	private static final long serialVersionUID = 2227786706936956528L;
	public String animation;
	public UnitIdentifier attacker, defender;
	public int damage;
	public int drain;
	
	public String toString(){
		return animation + ": " + attacker.name + ", " + defender.name + ", "
				+ damage + ", " + drain + " (drain)";
	}
}