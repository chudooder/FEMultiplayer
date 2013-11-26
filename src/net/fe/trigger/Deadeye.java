package net.fe.trigger;

import net.fe.FightStage;
import net.fe.RNG;
import net.fe.Trigger;
import net.fe.Unit;

public class Deadeye extends Trigger {
	public Deadeye(){
		super(true);
	}
	@Override
	public boolean runPreAttack(FightStage stage, Unit a, Unit d) {
		a.setTempMod("Crit", 100);
		return true;
	}
	@Override
	public void attempt(Unit user) {
		success = RNG.get() < user.get("Skl");
	}

}
