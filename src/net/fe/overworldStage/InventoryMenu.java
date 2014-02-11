package net.fe.overworldStage;

import java.util.Iterator;

import net.fe.unit.Item;
import net.fe.unit.ItemDisplay;
import net.fe.unit.Unit;

public class InventoryMenu extends ItemMenu {
	protected Unit unit;
	public InventoryMenu(Unit u, float x, float y) {
		super(x, y);
		unit = u;
		Iterator<Item> inv = unit.getInventory().iterator();
		for(int i = 0; i < 4; i++){
			if(inv.hasNext()){
				Item item = inv.next();
				items.add(new ItemDisplay(0,0, item, unit.getWeapon() == item));
			} else {
				items.add(null);
			}
		}
	}
	
	public InventoryMenu(Unit u){
		this(u,0,0);
	}
	public void onStep(){
		Iterator<Item> inv = unit.getInventory().iterator();
		for(int i = 0; i < 4; i++){
			if(inv.hasNext()){
				Item it = inv.next();
				ItemDisplay disp = items.get(i);
				if(disp.getItem() != it)
					items.set(i, new ItemDisplay(0,0, it, unit.getWeapon() == it));
			} else {
				items.set(i, null);
			}
		}
	}
}
