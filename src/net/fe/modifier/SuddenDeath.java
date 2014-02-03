package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

/**
 * All units start with 1 hp
 * @author Shawn
 *
 */
public class SuddenDeath implements Modifier{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4684401842583775643L;

	@Override
	public void modifyTeam(TeamBuilderStage stage) {
		// TODO Auto-generated method stub
		
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
			u.setHp(1);
		}
	}

	@Override
	public void endOfTurn(OverworldStage stage) {
		
	}

	@Override
	public String getDescription() {
		return "All units start at 1 HP.";
	}
	
	@Override
	public String toString() {
		return "Sudden Death";
	}

}
