package net.fe.modifier;

import net.fe.builderStage.ShopMenu;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.TeamSelectionStage;
import net.fe.fightStage.CombatTrigger;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

/**
 * All units have Miracle, with 100% chance to proc
 * @author Shawn
 *
 */
public class DivineIntervention implements Modifier {

	private static final long serialVersionUID = -7509901063099817137L;

	private class Miracle extends CombatTrigger {
		private static final long serialVersionUID = -8613896121666026506L;

		public Miracle() {
			super(APPEND_NAME_AFTER_MOD, ENEMY_TURN_MOD);
		}

		@Override
		public boolean attempt(Unit user, int range) {
			if(user.getHp() == 1) return false;
			return true;
		}
		
		@Override
		public int runDamageMod(Unit a, Unit d, int damage){
			if(d.getHp() - damage <= 0){
				return d.getHp() - 1;
			} else {
				return damage;
			}
		}

	}

	@Override
	public void modifyTeam(TeamBuilderStage stage) {
		
	}

	@Override
	public void modifyShop(ShopMenu shop) {
		
	}

	@Override
	public void modifyUnits(TeamSelectionStage stage) {
		for(Unit u : stage.getAllUnits()) {
			u.getTheClass().masterSkill = new Miracle();
		}
	}

	@Override
	public void initOverworld(OverworldStage stage) {
		
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
