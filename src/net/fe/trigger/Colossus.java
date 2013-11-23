package net.fe.trigger;

import net.fe.RNG;
import net.fe.Trigger;
import net.fe.Unit;

public class Colossus extends Trigger{
	public Colossus(){
		super(Type.PRE_ATTACK);
	}
	@Override
	public void attempt(Unit user) {
		success = RNG.get() < user.get("Skl")/2;
	}

	@Override
	public int run(Object... args) {
		Unit u = ((Unit)(args[1]));
		u.setTempMod("Str", u.get("Str"));
		return 0;
	}

}
