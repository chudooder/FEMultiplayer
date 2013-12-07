package net.fe.unit;


public abstract class Item {
	public final String name;
	private int maxUses;
	private int uses;
	public Item(String name){
		this.name = name;
	}
	public void use(Unit user){
		uses--;
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
}
