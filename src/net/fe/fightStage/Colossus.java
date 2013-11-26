package net.fe.fightStage;

import net.fe.RNG;
import net.fe.Trigger;
import net.fe.unit.Unit;

public class Colossus extends Trigger{
	public Colossus(){
		super(true);
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
