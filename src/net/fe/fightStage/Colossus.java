package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Colossus extends CombatTrigger{
	public Colossus(){
		super(REPLACE_NAME);
	}
	@Override
	public void attempt(Unit user) {
		success = RNG.get() < user.get("Skl")/2;
	}

	@Override
	public boolean runPreAttack(FightStage stage, Unit a, Unit d) {
		a.setTempMod("Str", a.get("Str"));
		return true;
	}

}
