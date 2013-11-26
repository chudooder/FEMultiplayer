package net.fe;

import java.util.Arrays;
import java.util.List;

public abstract class Trigger {
	public boolean success;
	public final boolean overridesName;
	public Trigger(boolean override){
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
