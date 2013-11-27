package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Pavise extends CombatTrigger {

	public Pavise() {
		super(APPEND_NAME);
	}

	@Override
	public void attempt(Unit user) {
		success = RNG.get() < user.get("Skl");
	}
	
	public int runDamageMod(Unit a, Unit d, int dmg){
		return dmg/2;
	}

}
