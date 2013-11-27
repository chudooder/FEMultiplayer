package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Luna extends CombatTrigger {
	public Luna(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE);
	}
	@Override
	public boolean attempt(Unit user) {
		return RNG.get() < user.get("Skl")/2;
	}

	@Override
	public boolean runPreAttack(FightStage stage, Unit a, Unit d) {
		d.setTempMod("Def", -d.get("Def"));
		return true;
	}

}
