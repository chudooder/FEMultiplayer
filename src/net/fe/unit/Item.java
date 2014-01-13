package net.fe.unit;

import java.io.Serializable;


public abstract class Item implements Serializable, Comparable<Item>{
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
	
	public abstract Item getCopy();
	
	public static Item getItem(String name){
		if(name.equals("Vulnerary")) return HealingItem.VULNERARY.getCopy();
		if(name.equals("Concoction")) return HealingItem.CONCOCTION.getCopy();
		if(name.equals("Elixir")) return HealingItem.ELIXIR.getCopy();
		return WeaponFactory.getWeapon(name);
	}
}
