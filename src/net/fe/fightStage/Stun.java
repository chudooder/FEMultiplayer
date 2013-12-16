package net.fe.fightStage;

import net.fe.RNG;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.StunTrigger;
import net.fe.unit.Unit;

public class Stun extends CombatTrigger{

	public Stun() {
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE);
	}

	@Override
	public boolean attempt(Unit user, int range) {
		return RNG.get() < user.get("Skl");
	}

	@Override
	public boolean runPreAttack(CombatCalculator calc, Unit a, Unit d) {
		((OverworldStage)d.stage).getTerrain(d.getXCoord(), d.getYCoord()).addTrigger(new StunTrigger());
		return true;
	}
	
	

}
