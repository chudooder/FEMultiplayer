package net.fe;

public abstract class Item {
	protected String name;
	protected int uses;
	public Item(String name){
		this.name = name;
	}
	public void use(Unit user){
		uses--;
	}
}
