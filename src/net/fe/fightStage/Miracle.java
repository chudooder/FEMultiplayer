package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Miracle extends CombatTrigger {
	
	public Miracle() {
		super(APPEND_NAME);
	}

	@Override
	public void attempt(Unit user) {
		success = RNG.get() < user.get("Lck");
	}
	
	public int runDamageMod(Unit a, Unit d, int damage){
		if(d.getHp() - damage <= 0){
			return d.getHp() - 1;
		} else {
			return damage;
		}
	}

}
