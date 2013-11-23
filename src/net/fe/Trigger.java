package net.fe;

import java.util.Arrays;
import java.util.List;

public abstract class Trigger {
	public boolean success;
	public final List<Type> type;
	public Trigger(Type... t){
		type = Arrays.asList(t);
	}
	public void clear(){
		success = false;
	}
	public abstract void attempt(Unit user);
	public abstract int run(Object... args);
	
	public enum Type{
		PRE_ATTACK, DAMAGE_MOD, POST_ATTACK, TURN_START, TURN_END
	}
}
