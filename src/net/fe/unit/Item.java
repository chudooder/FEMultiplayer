package net.fe.unit;


public abstract class Item {
	public final String name;
	protected int uses;
	public Item(String name){
		this.name = name;
	}
	public void use(Unit user){
		uses--;
	}
	public int getUses(){
		return uses;
	}
}
