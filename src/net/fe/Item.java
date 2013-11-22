package net.fe;

public abstract class Item {
	protected String name;
	protected int uses;
	public void use(Unit user){
		uses--;
	}
}
