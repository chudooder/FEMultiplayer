package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

// Everyone's hit rate is halved and crit rate doubled
public class Vegas implements Modifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3407505862142624494L;

	@Override
	public void modifyTeam(TeamBuilderStage stage) {
		
	}

	@Override
	public void modifyShop(ShopMenu shop) {
		
	}

	@Override
	public void modifyUnits(TeamSelectionStage stage) {
		
	}

	@Override
	public void initOverworld(OverworldStage stage) {
		for(Unit u : stage.getAllUnits()) {
			u.addSkill(new Gamble());
		}
	}

	@Override
	public void endOfTurn(OverworldStage stage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		return "Gamble! All units have halved hit rates and doubled crit rates.";
	}
	
	public String toString() {
		return "Vegas";
	}
	

}
