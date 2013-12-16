package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Luna extends CombatTrigger {
	private boolean ranged;
	public Luna(boolean rangeok){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE);
		ranged = rangeok;
	}
	@Override
	public boolean attempt(Unit user, int range) {
		return (ranged || range == 1) && RNG.get() < user.get("Skl")/2;
	}

	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		d.setTempMod("Def", -d.get("Def")/2);
		d.setTempMod("Res", -d.get("Res")/2);
		return true;
	}

}
