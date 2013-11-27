package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Lethality extends CombatTrigger {
	public Lethality(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE);
	}
	@Override
	public boolean attempt(Unit user) {
		return RNG.get() < user.get("Skl") / 4;
	}

	@Override
	public boolean runPreAttack(FightStage stage, Unit a, Unit d) {
		a.setTempMod("Str", 10000);
		return true;
	}

}
