package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Aether extends CombatTrigger {
	private int phase;
	
	private static final int SOL = 0;
	private static final int LUNA = 2;
	
	
	public Aether(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE + YOUR_TURN_POST);
	}
	@Override
	public boolean attempt(Unit user) {
		return RNG.get() < user.get("Skl")/2 || phase != SOL;
	}
	@Override
	public boolean runPreAttack(FightStage stage, Unit a, Unit d) {
		if(phase == LUNA){
			new Luna().runPreAttack(stage, a, d);
		}
		return true;
	}
	@Override
	public void runPostAttack(FightStage stage, boolean dir, Unit a, Unit d,
			int damage, String currentEffect) {
		if(phase == SOL){
			if(damage == 0) return;
			int heal = Math.min(damage/2, a.get("HP") - a.getHp());
			stage.addToAttackQueue(a, a, "Aether2(a)", -heal);
			a.setHp(a.getHp() + damage/2);
			if(d.getHp() > 0){
				phase = LUNA;
				stage.attack(dir, "Aether");
			}
		} else {
			phase = SOL;
		}
	}
	
	public String getName(){
		return "Aether" + (phase + 1);
	}
}
