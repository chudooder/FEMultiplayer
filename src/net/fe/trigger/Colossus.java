package net.fe.trigger;

import net.fe.FightStage;
import net.fe.RNG;
import net.fe.Trigger;
import net.fe.Unit;

public class Colossus extends Trigger{
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
