package net.fe.fightStage;

import net.fe.unit.Unit;

public class Nosferatu extends CombatTrigger{
	private static final long serialVersionUID = 1L;

	public Nosferatu() {
		super(NO_NAME_MOD, YOUR_TURN_PRE + YOUR_TURN_DRAIN);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean attempt(Unit user, int range) {
		return true;
	}
	
	public int runDrain(Unit a, Unit d, int damage){
		if (damage == 0)
			return 0;
		return Math.min(damage / 2, a.get("HP") - a.getHp());
	}
	
	public CombatTrigger getCopy(){
		return new Nosferatu();
	}
}
