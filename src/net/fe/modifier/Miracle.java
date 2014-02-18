package net.fe.modifier;

import net.fe.fightStage.CombatTrigger;
import net.fe.unit.Unit;

public class Miracle extends CombatTrigger {
	private static final long serialVersionUID = -8613896121666026506L;

	public Miracle() {
		super(APPEND_NAME_AFTER_MOD, ENEMY_TURN_MOD);
	}

	@Override
	public boolean attempt(Unit user, int range) {
		if(user.getHp() == 1) return false;
		return true;
	}
	
	@Override
	public int runDamageMod(Unit a, Unit d, int damage){
		if(d.getHp() - damage <= 0){
			return d.getHp() - 1;
		} else {
			return damage;
		}
	}
	
	public CombatTrigger getCopy(){
		return new Miracle();
	}

}
