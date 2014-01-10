package net.fe.unit;

import java.io.Serializable;


public abstract class Item implements Serializable {
	private static final long serialVersionUID = 210303763886733870L;
	public final String name;
	private int maxUses;
	private int uses;
	private int cost;
	public int id;
	public Item(String name){
		this.name = name;
	}
	int use(Unit user){
		uses--;
		return 0;
	}
	public int getUses(){
		return uses;
	}
	public void setMaxUses(int x){
		uses = x;
		maxUses = x;
	}
	public int getMaxUses(){
		return maxUses;
	}
	
	public void setUsesDEBUGGING(int uses){
		this.uses = uses;
	}
	
	public int getCost(){
		return cost;
	}
	
	public void setCost(int gold){
		cost = gold;
	}
}
