package net.fe.modifier;

import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.CombatTrigger;
import net.fe.unit.Unit;

public class Gamble extends CombatTrigger {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5208500314150806972L;
	public Gamble(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE, "gamble");
	}
	@Override
	public boolean runPreAttack(CombatCalculator stage, Unit a, Unit d) {
		a.setTempMod("Crit", a.crit());
		a.setTempMod("Hit", -a.hit()/2);
		return true;
	}
	@Override
	public boolean attempt(Unit user, int range) {
		return true;
	}
	
	public CombatTrigger getCopy(){
		return new Gamble();
	}

}
