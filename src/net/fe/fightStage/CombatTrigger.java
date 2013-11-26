package net.fe.fightStage;

import java.util.Arrays;
import java.util.List;

import net.fe.unit.Unit;

public abstract class CombatTrigger {
	public boolean success;
	public final boolean overridesName;
	public CombatTrigger(boolean override){
		overridesName = override;
	}
	public void clear(){
		success = false;
	}
	public abstract void attempt(Unit user);
	public boolean runPreAttack(FightStage stage, Unit a, Unit d){
		return true;
	}
	public int runDamageMod(int damage){
		return damage;
	}
	public void runPostAttack(FightStage stage, Unit a, Unit d, int damage){
		
	}
}
