package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

/**
 * All units have Miracle, with 100% chance to proc
 * @author Shawn
 *
 */
public class DivineIntervention implements Modifier {

	private static final long serialVersionUID = -7509901063099817137L;

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
			u.addSkill(new Intervention());
		}
	}

	@Override
	public void endOfTurn(OverworldStage stage) {
		
	}
	
	@Override
	public String toString() {
		return "Divine Intervention";
	}
	
	@Override
	public String getDescription() {
		return "All units have a version of Miracle that is guarenteed to activate.";
	}
}
