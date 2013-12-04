package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Sol extends CombatTrigger {
	public Sol(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE + YOUR_TURN_POST);
	}
	@Override
	public boolean attempt(Unit user) {
		
		return RNG.get() < user.get("Skl");
	}
	@Override
	public void runPostAttack(FightStage stage, boolean dir, Unit a, Unit d, int damage, String currentEffect){
		if(damage == 0) return;
		System.out.println("We're here");
		int heal = Math.min(damage/2, a.get("HP") - a.getHp());
		stage.addToAttackQueue(a, a, "Sol2(a)", -heal);
		a.setHp(a.getHp() + damage/2);
	}
	@Override
	public String getName(){
		return "Sol1";
	}
}
