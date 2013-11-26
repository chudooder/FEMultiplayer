package net.fe.overworldStage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.fe.unit.Class;
import net.fe.unit.Unit;

public enum Terrain {
	PLAIN(1,0,0,0), 
	FOREST(2,1,20,0), 
	FLOOR(1,0,0,0), 
	PILLAR(2,1,20,0), 
	MOUNTAIN(4,2,30,0), 
	PEAK(127,2,40,0), 
	FORT(2,1,15,10), 
	SEA(127,0,10,0), 
	DESERT(2,0,5,0),
	WALL(127,0,0,0);

	private int baseMoveCost;
	public final int avoidBonus;
	public final int defenseBonus;
	public final int healthBonus;
	private CopyOnWriteArrayList<TerrainTrigger> triggers;

	Terrain(int baseMoveCost, int avo, int def, int health) {
		this.baseMoveCost = baseMoveCost;
		avoidBonus = avo;
		defenseBonus = def;
		healthBonus = health;
		triggers = new CopyOnWriteArrayList<TerrainTrigger>();
		if(healthBonus > 0){
			triggers.add(new Healing(healthBonus));
		}
	}

	public int getMoveCost(Class c) {
		if (c == null)
			return baseMoveCost;
		String name = c.name;
		if(c.equals("Falcon Knight")){
			return 1;
		}
		
		else if (this == SEA) {
			if (name.equals("Berserker")) {
				return 2;
			}
		}
		
		else if (this == FOREST || this == PILLAR) {
			if (name.equals("Sniper") || name.equals("Paladin")) {
				return 3;
			}
		}
		
		else if (this == DESERT) {
			if (name.equals("Sniper") || name.equals("General")) {
				return 3;
			}
			else if (name.equals("Paladin")) {
				return 4;
			}
		}
		
		else if (this == MOUNTAIN) {
			if (name.equals("Berserker")
					|| name.equals("Hero")
					|| name.equals("Sniper")
					|| name.equals("Swordmaster")) {
				return 3;
			}
			else if(name.equals("Paladin")) {
				return 6;
			}
		}
		
		return baseMoveCost;
	}
	
	public void addTrigger(TerrainTrigger e){
		triggers.add(e);
	}
	
	public void removeTrigger(TerrainTrigger e){
		triggers.remove(e);
	}
	
	public List<TerrainTrigger> getTriggers(){
		return triggers;
	}
	
	public class Healing extends TerrainTrigger{
		private int percent;
		public Healing(int percent){
			this.percent = percent;
		}
		public void startOfTurn(Grid g, int x, int y){
			Unit u = g.getUnit(x, y);
			u.setHp(u.getHp() + u.get("HP")*percent/100);
		}
	}
}
