package net.fe.fightStage;

import net.fe.unit.Unit;

public abstract class CombatTrigger {
	public final int nameModification;
	public final int turnToRun;
	
	public static final int NO_NAME_MOD = 0;
	public static final int REPLACE_NAME_AFTER_PRE = 1;
	public static final int APPEND_NAME_AFTER_MOD = 2;
	
	public static final int YOUR_TURN_PRE = 0x1;
	public static final int ENEMY_TURN_PRE = 0x2;
	public static final int YOUR_TURN_MOD = 0x4;
	public static final int ENEMY_TURN_MOD = 0x8;
	public static final int YOUR_TURN_POST = 0x10;
	public static final int ENEMY_TURN_POST = 0x20;
	public static final int YOUR_TURN_DRAIN = 0x40;
	public static final int ENEMY_TURN_DRAIN = 0x80;
	
	public CombatTrigger(int mod, int turn){
		nameModification = mod;
		turnToRun = turn;
	}
	public abstract boolean attempt(Unit user, int range);
	public boolean runPreAttack(CombatCalculator calc, Unit a, Unit d){
		return true;
	}
	public int runDamageMod(Unit a, Unit d, int damage){
		return damage;
	}
	public int runDrain(Unit a, Unit d, int damage){
		return 0;
	}
	public void runPostAttack(CombatCalculator calc, boolean dir, Unit a, Unit d, int damage, String currentEffect){
		
	}
	
	public String getName(){
		return getClass().getSimpleName();
	}
	
	
}
