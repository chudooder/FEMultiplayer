package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Pavise extends CombatTrigger {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5629273200054077795L;

	public Pavise() {
		super(APPEND_NAME_AFTER_MOD, ENEMY_TURN_MOD);
	}

	@Override
	public boolean attempt(Unit user, int range) {
		//return true;
		return RNG.get() < user.get("Skl");
	}
	
	@Override
	public int runDamageMod(Unit a, Unit d, int dmg){
		return dmg/2;
	}

}
