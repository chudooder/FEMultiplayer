package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Sol extends CombatTrigger {
	private boolean ranged;
	public Sol(boolean rangeok) {
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE + YOUR_TURN_DRAIN);
		ranged = rangeok;
	}

	@Override
	public boolean attempt(Unit user, int range) {

		return (ranged || range == 1) && RNG.get() < user.get("Skl");
	}
	
	public int runDrain(Unit a, Unit d, int damage){
		if (damage == 0)
			return 0;
		return Math.min(damage / 2, a.get("HP") - a.getHp());
	}
}
