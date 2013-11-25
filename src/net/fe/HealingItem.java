package net.fe;

public class HealingItem extends Item {
	public static HealingItem VULNERARY = new HealingItem("Vulnerary", 10);
	public static HealingItem CONCOCTION = new HealingItem("Concoction", 20);
	public static HealingItem ELIXIR = new HealingItem("Elixir", 99);
	
	public int amount;
	public HealingItem(String name, int amount){
		super(name);
		uses = 3;
		this.amount = amount;
	}
	public void use(Unit user){
		//TODO implement healing items
	}
}
