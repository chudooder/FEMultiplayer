package net.fe.fightStage;

import net.fe.unit.Unit;

public class Crisis extends CombatTrigger{
	private static final long serialVersionUID = 1L;

	public Crisis() {
		super(NO_NAME_MOD, YOUR_TURN_PRE + ENEMY_TURN_PRE);
	}

	@Override
	public boolean attempt(Unit user, int range) {
		return user.getHp() < user.get("HP")/3;
	}

	@Override
	public boolean runPreAttack(CombatCalculator calc, Unit a, Unit d) {
		a.setTempMod("Avo", 30);
		return true;
	}
	
	

}
