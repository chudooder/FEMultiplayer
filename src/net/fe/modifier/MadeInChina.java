package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = -3929819526675171008L;

	@Override
	public void modifyTeam(TeamBuilderStage stage) {
		
	}
	
	@Override
	public void modifyShop(ShopMenu shop) {

	}

	@Override
	public void initOverworld(OverworldStage stage) {
		for(Unit u : stage.getAllUnits()) {
			for(Item item : u.getInventory()) {
				if(item instanceof Weapon) {
					item.setUsesDEBUGGING(2);
				}
			}
		}
	}

	@Override
	public void endOfTurn(OverworldStage stage) {
		
	}

	@Override
	public void modifyUnits(TeamSelectionStage stage) {
		
	}
	
	@Override
	public String toString() {
		return "Made In China";
	}
	
	@Override
	public String getDescription() {
		return "All weapons have greatly reduced durability. Start with extra gold.";
	}

}
