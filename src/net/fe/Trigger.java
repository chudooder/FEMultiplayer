package net.fe;

public abstract class Trigger<E> {
	private int chance;
	protected boolean success;
	protected Type type;
	public Trigger(int chance){
		this.chance = chance;
	}
	public Trigger(){
		this(100);
	}
	public void clear(){
		success = false;
	}
	public void attempt(){
		if(RNG.get() < chance){
			success = true;
		}
	}
	public abstract E run(Object[] args);
	
	public enum Type{
		PRE_ATTACK, DAMAGE_MOD, POST_ATTACK, TURN_START, TURN_END
	}
}
