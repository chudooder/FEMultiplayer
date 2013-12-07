package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Colossus extends CombatTrigger{
	public Colossus(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE);
	}
	@Override
	public boolean attempt(Unit user) {
		return RNG.get() < user.get("Skl")/2;
	}

	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		a.setTempMod("Str", a.get("Str"));
		return true;
	}

}
