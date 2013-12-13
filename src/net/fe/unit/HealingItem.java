package net.fe.unit;


public class HealingItem extends Item {
	public static HealingItem VULNERARY = new HealingItem("Vulnerary", 10, 67);
	public static HealingItem CONCOCTION = new HealingItem("Concoction", 20, 68);
	public static HealingItem ELIXIR = new HealingItem("Elixir", 99, 69);
	
	public int amount;
	public HealingItem(String name, int amount, int id){
		super(name);
		setMaxUses(3);
		this.amount = amount;
		this.id = id;
	}
	public int use(Unit user){
		super.use(user);
		user.setHp(user.getHp() + amount);
		return amount;
		//TODO implement healing items
	}
}
