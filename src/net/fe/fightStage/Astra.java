package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Astra extends CombatTrigger {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2363437411973357900L;
	private transient int counter;
	public Astra(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE + YOUR_TURN_MOD + YOUR_TURN_POST, 
				"astra1", "astra2", "astra3", "astra4", "astra5");
	}
	@Override
	public boolean attempt(Unit user, int range) {
	//	return true;
		return range == 1 && (RNG.get() < user.get("Skl")/2 || counter!=0);
	}
	
	@Override
	public int runDamageMod(Unit a, Unit d, int damage){
		return damage/2;
	}
	@Override
	public void runPostAttack(CombatCalculator calc, boolean dir, Unit a, Unit d, int damage,
			String currentEffect) {
		if(counter == 4){
			//its the last hit
			counter = 0;
		} else {
			counter++;
			if(d.getHp() > 0){
				calc.addAttack("Astra");
			}
		}
	}
	@Override
	public String getName() {
		return super.getName() + (counter+1);
	}
	

}
