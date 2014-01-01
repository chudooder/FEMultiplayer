package net.fe.overworldStage.context;

import chu.engine.anim.AudioPlayer;
import net.fe.overworldStage.Healthbar;
import net.fe.overworldStage.InventoryMenu;
import net.fe.overworldStage.MenuContext;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.HealingItem;
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
		AudioPlayer.playAudio("select", 1, 1);
		if(i instanceof Weapon){
			if(unit.equippable((Weapon) i)){
				unit.equip((Weapon)i);
				menu.setSelection(0);
			}
		} else if (i instanceof HealingItem){
			if(unit.getHp() == unit.get("HP")) return;
			stage.addCmd("Use");
			stage.addCmd(unit.findItem(i));
			stage.send();
			
			stage.setMenu(null);
			
			int oHp = unit.getHp();
			unit.use(i);
			//TODO Positioning
			stage.addEntity(new Healthbar(
					320, 20 , oHp, 
					unit.getHp(), unit.get("HP")){
				@Override
				public void done() {
					destroy();
					unit.moved();
					ItemCmd.this.stage.reset();
				}
			});
		}
	}

	@Override
	public void onLeft() {
		
	}
	@Override
	public void onRight() {
		
	}
	
}
