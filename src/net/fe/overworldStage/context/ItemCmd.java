package net.fe.overworldStage.context;

import net.fe.overworldStage.InventoryMenu;
import net.fe.overworldStage.MenuContext;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Item;
import net.fe.unit.ItemDisplay;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;

public class ItemCmd extends MenuContext<ItemDisplay>{
	private Unit unit;
	public ItemCmd(OverworldStage stage, OverworldContext prev, Unit u) {
		super(stage, prev, new InventoryMenu(u));
		unit = u;
	}
	@Override
	public void onSelect(ItemDisplay selectedItem) {
		Item i = selectedItem.getItem();
		if(i instanceof Weapon){
			unit.equip((Weapon)i);
		} else {
			//Healing
		}
	}
	@Override
	public void onCancel() {
		stage.setMenu(null);
		prev.startContext();
	}
	@Override
	public void onLeft() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onRight() {
		// TODO Auto-generated method stub
		
	}
	
}
