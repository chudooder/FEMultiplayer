package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Lethality extends CombatTrigger {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7524008345607446540L;

	public Lethality(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE);
	}
	@Override
	public boolean attempt(Unit user, int range) {
//		return true;
		return RNG.get() < user.get("Skl") / 3;
	}

	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		a.setTempMod("Str", 9000);
		a.setTempMod("Hit", 9000);
		return true;
	}

}
