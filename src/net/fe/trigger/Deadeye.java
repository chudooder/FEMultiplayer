package net.fe.trigger;

import net.fe.RNG;
import net.fe.Trigger;
import net.fe.Unit;

public class Deadeye extends Trigger {
	public Deadeye(){
		super(Trigger.Type.PRE_ATTACK);
	}
	@Override
	public int run(Object... args) {
		((Unit)(args[1])).setTempMod("Crit", 100);
		return 0;
	}
	@Override
	public void attempt(Unit user) {
		success = RNG.get() < user.get("Skl");
	}

}
