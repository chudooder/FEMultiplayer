package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;

public class Veterans implements Modifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8924524348358477808L;

	@Override
	public void modifyTeam(TeamBuilderStage stage) {
		stage.setExp(999999999);
	}

	@Override
	public void modifyShop(ShopMenu shop) {
		
	}

	@Override
	public void modifyUnits(TeamSelectionStage stage) {
		
	}

	@Override
	public void initOverworld(OverworldStage stage) {
		
	}

	@Override
	public void endOfTurn(OverworldStage stage) {
		
	}

	@Override
	public String getDescription() {
		return "Unlimited starting EXP.";
	}
	
	public String toString() {
		return "Veterans";
	}
	
}
