package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;

public interface Modifier {
	
	public void modifyTeam(TeamBuilderStage stage);
	public void modifyShop(ShopMenu shop);
	public void modifyUnits(TeamSelectionStage stage);
	public void initOverworld(OverworldStage stage);
	public void endOfTurn(OverworldStage stage);

}
