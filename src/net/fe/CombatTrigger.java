package net.fe;

public abstract class CombatTrigger extends Trigger{
	private int chance;
	protected boolean success;
	public CombatTrigger(int chance){
		this.chance = chance;
	}
	public void clear(){
		success = false;
	}
	public void attempt(){
		if(RNG.get() < chance){
			success = true;
		}
	}
	public abstract int run(Unit a, Unit d);
}
