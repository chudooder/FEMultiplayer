package net.fe.trigger;

import net.fe.FightStage;
import net.fe.RNG;
import net.fe.Trigger;
import net.fe.Unit;

public class Lethality extends Trigger {
	@Override
	public void attempt(Unit user) {
		success = RNG.get() < user.get("Skl") / 4;
	}

	@Override
	public boolean runPreAttack(FightStage stage, Unit a, Unit d) {
		a.setTempMod("Str", 10000);
		return true;
	}

}
