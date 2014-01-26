package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.ItemMenu;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Item;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;

/**
 * All weapons have 2 durability. Players are given
 * extra gold to compensate.
 * @author Shawn
 *
 */
public class MadeInChina implements Modifier {

	@Override
	public void modifyTeam(TeamBuilderStage stage) {
		stage.setFunds((int)(TeamBuilderStage.FUNDS * 1.25));
	}
	
	@Override
	public void modifyShop(ShopMenu shop) {
		for(ItemMenu menu : shop.getShops()) {
			for(int i=0; i<menu.size(); i++) {
				Item item = menu.get(i).getItem();
				if(item instanceof Weapon) {
					item.setUsesDEBUGGING(2);
				}
			}
		}
	}

	@Override
	public void initOverworld(OverworldStage stage) {
		
	}

	@Override
	public void endOfTurn(OverworldStage stage) {
		
	}

	@Override
	public void modifyUnits(TeamSelectionStage stage) {
		/* Lords come with weapons, so we need to set the durability
		 of those as well */
		for(Unit u : stage.getAllUnits()) {
			for(Item item : u.getInventory()) {
				if(item instanceof Weapon) {
					item.setUsesDEBUGGING(2);
				}
			}
		}
	}

}
