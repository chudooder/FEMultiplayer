package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;

public class Treasury implements Modifier {

	private static final long serialVersionUID = 843174984852438018L;

	@Override
	public void modifyTeam(TeamBuilderStage stage) {
		stage.setFunds(99999900);
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
	public String toString() {
		return "Treasury";
	}
	
	@Override
	public String getDescription() {
		return "Start with the maximum amount of gold.";
	}

}
