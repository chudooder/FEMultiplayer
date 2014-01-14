package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Aether extends CombatTrigger {
	private transient int phase;
	
	private static final int SOL = 0;
	private static final int LUNA = 1;
	
	
	public Aether(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE + YOUR_TURN_POST + YOUR_TURN_DRAIN);
	}
	@Override
	public boolean attempt(Unit user, int range) {
		return range == 1 && (RNG.get() < user.get("Skl")/2 || phase != SOL);
	}
	@Override
	public boolean runPreAttack(CombatCalculator calc, Unit a, Unit d) {
		if(phase == LUNA){
			new Luna(false).runPreAttack(calc, a, d);
		}
		return true;
	}
	@Override
	public int runDrain(Unit a, Unit d, int damage){
		if(phase == SOL){
			if(damage == 0) return 0;
			return Math.min(damage/2, a.get("HP") - a.getHp());
		} else {
			return 0;
		}
	}
	@Override
	public void runPostAttack(CombatCalculator calc, boolean dir, Unit a, Unit d,
			int damage, String currentEffect) {
		if(phase == SOL){
			if(d.getHp() > 0){
				phase = LUNA;
				calc.addAttack("Aether");
			}
		} else {
			phase = SOL;
		}
	}
	
	public String getName(){
		return "Aether" + (phase + 1);
	}
}
