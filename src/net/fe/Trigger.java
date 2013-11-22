package net.fe;

public abstract class Trigger<E> {
	private int chance;
	protected boolean success;
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
}
