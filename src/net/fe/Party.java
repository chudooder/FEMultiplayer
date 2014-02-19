package net.fe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.Color;

import net.fe.unit.Unit;

public class Party implements Iterable<Unit>, Serializable{
	private static final long serialVersionUID = 1334090578185765598L;
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
		color = TEAM_BLUE;
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

	public List<Unit> getUnits() {
		return units;
	}
	
	public Unit search(String name) {
		for(Unit u : units) {
			if(u.name.equals(name)){
				return u;
			}
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

	@Override
	public Iterator<Unit> iterator() {
		return units.iterator();
	}

	public void clear() {
		units.clear();
	}

}
