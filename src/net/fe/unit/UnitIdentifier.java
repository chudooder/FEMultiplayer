package net.fe.unit;

import java.io.Serializable;

public class UnitIdentifier implements Serializable{
	private static final long serialVersionUID = 1L;
	public String name;
	public org.newdawn.slick.Color partyColor;
	
	public UnitIdentifier(Unit u){
		name = u.name;
		partyColor = u.getPartyColor();
	}
}
