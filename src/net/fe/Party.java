package net.fe;

import java.util.ArrayList;

import net.fe.unit.Unit;

public class Party {
	
	private ArrayList<Unit> units;
	
	public Party() {
		units = new ArrayList<Unit>();
	}
	
	public Party(ArrayList<Unit> units) {
		this.units = units;
	}
	
	public void addUnit(Unit unit) {
		units.add(unit);
	}
	
	public Unit getUnit(int index) {
		return units.get(index);
	}
	
	public Unit search(String name) {
		for(Unit u : units) {
			if(u.name.equals(name)) return u;
		}
		return null;
	}
	
	public int size() {
		return units.size();
	}

}
