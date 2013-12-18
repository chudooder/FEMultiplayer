package net.fe.unit;


public abstract class Item {
	public final String name;
	private int maxUses;
	private int uses;
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
}
