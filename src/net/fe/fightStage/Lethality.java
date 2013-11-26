package net.fe.fightStage;

import net.fe.RNG;
import net.fe.Trigger;
import net.fe.unit.Unit;

public class Lethality extends Trigger {
	public Lethality(){
		super(true);
	}
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
