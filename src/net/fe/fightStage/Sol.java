package net.fe.fightStage;

import net.fe.RNG;
import net.fe.Trigger;
import net.fe.unit.Unit;

public class Sol extends Trigger {
	public Sol(){
		super(true);
	}
	@Override
	public void attempt(Unit user) {
		success = RNG.get() < user.get("Skl");
	}

	@Override
	public boolean runPreAttack(FightStage stage, Unit a, Unit d) {
		return true;
	}
	
	public void runPostAttack(FightStage stage, Unit a, Unit d, int damage){
		stage.addToAttackQueue(a, a, "Sol2", -damage/2);
		a.setHp(a.getHp() + damage/2);
	}
}
