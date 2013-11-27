package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Astra extends CombatTrigger {
	private int counter;
	public Astra(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE + YOUR_TURN_MOD + YOUR_TURN_POST);
	}
	@Override
	public boolean attempt(Unit user) {
		return RNG.get() < user.get("Skl")/2 || counter!=0;
	}
	
	@Override
	public int runDamageMod(Unit a, Unit d, int damage){
		return damage/2;
	}
	@Override
	public void runPostAttack(FightStage stage, boolean dir, Unit a, Unit d, int damage) {
		if(counter == 4){
			//its the last hit
			counter = 0;
		} else {
			counter++;
			stage.attack(dir);
		}
	}
	@Override
	public String getName() {
		return super.getName() + (counter+1);
	}
	

}
