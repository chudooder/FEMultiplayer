package net.fe.overworldStage;

import java.util.Iterator;

import net.fe.unit.Item;
import net.fe.unit.ItemDisplay;
import net.fe.unit.Unit;

public class InventoryMenu extends ItemMenu {
	private Unit unit;
	public InventoryMenu(Unit u, float x, float y) {
		super(x, y);
		unit = u;
	}
	
	public InventoryMenu(Unit u){
		this(u,0,0);
	}
	public void onStep(){
		items.clear();
		Iterator<Item> inv = unit.getInventory().iterator();
		for(int i = 0; i < 4; i++){
			if(inv.hasNext()){
				Item it = inv.next();
				items.add(new ItemDisplay(0,0, it, unit.getWeapon() == it));
			} else {
				items.add(null);
			}
		}
	}
}
