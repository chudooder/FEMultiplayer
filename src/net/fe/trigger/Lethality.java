package net.fe.trigger;

import net.fe.RNG;
import net.fe.Trigger;
import net.fe.Unit;

public class Lethality extends Trigger {
	public Lethality(){
		super(Type.PRE_ATTACK);
	}
	@Override
	public void attempt(Unit user) {
		success = RNG.get() < 50;
	}

	@Override
	public int run(Object... args) {
		((Unit)args[1]).setTempMod("Str", 10000);
		return 0;
	}

}
