package net.fe.overworldStage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.fe.Player;
import net.fe.unit.Class;
import net.fe.unit.Unit;
import net.fe.unit.WeaponFactory;
import chu.engine.Entity;

public enum Terrain {
	PLAIN(1,0,0,0),
	PATH(1,0,0,0),
	FOREST(2,1,20,0), 
	FLOOR(1,0,0,0), 
	PILLAR(2,1,20,0), 
	MOUNTAIN(4,2,30,0),
	VILLAGE(1,10,0,0),
	PEAK(127,2,40,0), 
	FORT(2,1,15,10), 
	SEA(127,0,10,0), 
	DESERT(2,0,5,0),
	WALL(127,0,0,0), 
	FENCE(127,0,0,0),
	NONE(127,0,0,0), 
	CLIFF(127,0,0,0),
	THRONE(1,3,30,10), 
	HILL(4,2,30,0),
	HOUSE(127,0,10,0);

	private int baseMoveCost;
	private final int avoidBonus;
	private final int defenseBonus;
	public final int healthBonus;
	private CopyOnWriteArrayList<TerrainTrigger> triggers;
	
	

	Terrain(int baseMoveCost, int def, int avo, int health) {
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
		if(WeaponFactory.fliers.contains(name)){
			if(this == WALL)
				return 127;
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
		
		else if (this == MOUNTAIN || this == HILL) {
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
	
	public int getAvoidBonus(Unit u) {
		if(u == null) return avoidBonus;
		if(WeaponFactory.fliers.contains(u.getTheClass().name)) return 0;
		return avoidBonus;
	}
	
	public int getDefenseBonus(Unit u) {
		if(u == null) return defenseBonus;
		if(WeaponFactory.fliers.contains(u.getTheClass().name)) return 0;
		return defenseBonus;
	}
	
	public class Healing extends TerrainTrigger{
		private int percent;
		private int amount;
		public Healing(int percent){
			super(true);
			this.percent = percent;
		}
		public boolean attempt(OverworldStage g, int x, int y, Player turnPlayer){
			Unit u = g.getUnit(x, y);
			return u != null && 
					u.getPartyColor().equals(turnPlayer.getParty().getColor()) &&
					u.get("HP") > u.getHp();
		}
		public void startOfTurn(OverworldStage g, int x, int y){
			Unit u = g.getUnit(x, y);
			amount = Math.min(u.get("HP")*percent/100, u.get("HP") - u.getHp());
			if(u != null)
				u.setHp(u.getHp() + amount);
		}
		public Entity getAnimation(OverworldStage g, int x, int y){
			Unit u = g.getUnit(x, y);
			return new Healthbar(u, u.getHp(), u.getHp() + amount, (ClientOverworldStage) g){
				@Override
				public void done() {
					destroy();
				}
			};
		}
	}
}
