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
	
	public int hashCode(){
		return name.hashCode() + partyColor.hashCode();
	}
	
	public boolean equals(Object o){
		if(!(o instanceof UnitIdentifier)) return false;
		UnitIdentifier other = (UnitIdentifier) o;
		return name.equals(other.name) && partyColor.equals(other.partyColor);
	}
	
	public String toString(){
		return name;
	}
}
