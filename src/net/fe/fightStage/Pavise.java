package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Pavise extends CombatTrigger {

	public Pavise() {
		super(APPEND_NAME_AFTER_MOD, ENEMY_TURN_MOD);
	}

	@Override
	public boolean attempt(Unit user) {
		return RNG.get() < user.get("Skl");
	}
	
	public int runDamageMod(Unit a, Unit d, int dmg){
		return dmg/2;
	}

}
