package net.fe;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import net.fe.unit.Unit;

public class Party {
	private Color color;
	private ArrayList<Unit> units;
	private ArrayList<Party> allies;
	
	public static final Color TEAM_RED = new Color(220,0,0);
	public static final Color TEAM_GREEN = new Color(0,190,0);
	public static final Color TEAM_BLUE = new Color(0,0,220);
	
	public Party() {
		units = new ArrayList<Unit>();
		allies = new ArrayList<Party>();
		allies.add(this);
	}
	
	public Party(ArrayList<Unit> units) {
		this.units = units;
		allies = new ArrayList<Party>();
		allies.add(this);
	}
	
	public void addAlly(Party p){
		allies.add(p);
	}
	
	public boolean isAlly(Party p){
		return allies.contains(p);
	}
	
	public void addUnit(Unit unit) {
		units.add(unit);
		unit.setParty(this);
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
	
	public void setColor(Color c){
		color = c;
	}
	
	public Color getColor(){
		return color;
	}

}
