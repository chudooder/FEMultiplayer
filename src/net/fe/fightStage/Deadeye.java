package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Deadeye extends CombatTrigger {
	public Deadeye(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE);
	}
	@Override
	public boolean runPreAttack(FightStage stage, Unit a, Unit d) {
		a.setTempMod("Crit", 9000);
		return true;
	}
	@Override
	public boolean attempt(Unit user) {
		return RNG.get() < user.get("Skl");
	}

}
