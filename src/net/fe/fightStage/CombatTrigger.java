package net.fe.fightStage;

import java.util.Arrays;
import java.util.List;

import net.fe.unit.Unit;

public abstract class CombatTrigger {
	public boolean success;
	public final int nameModification;
	
	public static final int NO_MOD = 0;
	public static final int REPLACE_NAME = 1;
	public static final int APPEND_NAME = 2;
	
	public CombatTrigger(int mod){
		nameModification = mod;
	}
	public void clear(){
		success = false;
	}
	public abstract void attempt(Unit user);
	public boolean runPreAttack(FightStage stage, Unit a, Unit d){
		return true;
	}
	public int runDamageMod(Unit a, Unit d, int damage){
		return damage;
	}
	public void runPostAttack(FightStage stage, Unit a, Unit d, int damage){
		
	}
}
