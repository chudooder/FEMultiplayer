package net.fe.overworldStage.context;

import net.fe.overworldStage.InventoryMenu;
import net.fe.overworldStage.MenuContext;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.ItemDisplay;
import net.fe.unit.Unit;

public class Item extends MenuContext<ItemDisplay>{
	private Unit unit;
	public Item(OverworldStage stage, OverworldContext prev, Unit u) {
		super(stage, prev, new InventoryMenu(u));
		unit = u;
	}
	@Override
	public void onSelect(ItemDisplay selectedItem) {
		// TODO Auto-generated method stub
		
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
