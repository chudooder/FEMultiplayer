package net.fe.fightStage;

import java.util.Arrays;
import java.util.List;

import net.fe.unit.Unit;

public abstract class CombatTrigger {
	public final int nameModification;
	public final int turnToRun;
	
	public static final int NO_MOD = 0;
	public static final int REPLACE_NAME_AFTER_PRE = 1;
	public static final int APPEND_NAME_AFTER_MOD = 2;
	
	public static final int YOUR_TURN_PRE = 0x1;
	public static final int ENEMY_TURN_PRE = 0x2;
	public static final int YOUR_TURN_MOD = 0x4;
	public static final int ENEMY_TURN_MOD = 0x8;
	public static final int YOUR_TURN_POST = 0x10;
	public static final int ENEMY_TURN_POST = 0x20;
	
	public CombatTrigger(int mod, int turn){
		nameModification = mod;
		turnToRun = turn;
	}
	public abstract boolean attempt(Unit user);
	public boolean runPreAttack(FightStage stage, Unit a, Unit d){
		return true;
	}
	public int runDamageMod(Unit a, Unit d, int damage){
		return damage;
	}
	public void runPostAttack(FightStage stage, boolean dir, Unit a, Unit d, int damage){
		
	}
	
	public String getName(){
		return getClass().getSimpleName();
	}
	
	
}
