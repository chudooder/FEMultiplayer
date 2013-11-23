package net.fe.trigger;

import net.fe.RNG;
import net.fe.Trigger;
import net.fe.Unit;

public class Luna extends Trigger {
	public Luna(){
		super(Type.PRE_ATTACK);
	}
	@Override
	public void attempt(Unit user) {
		success = RNG.get() < 50;// user.get("Skl")/2;
	}

	@Override
	public int run(Object... args) {
		Unit u = ((Unit)(args[2]));
		u.setTempMod("Def", -u.get("Def"));
		return 0;
	}

}
