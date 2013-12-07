package net.fe.overworldStage;

public abstract class OverworldContext {
	protected OverworldStage stage;
	protected OverworldContext prev;
	public OverworldContext(OverworldStage s, OverworldContext prevContext){
		stage = s;
		prev = prevContext;
	}
	public abstract void onSelect();
	public abstract void onCancel();
	public abstract void onUp();
	public abstract void onDown();
	public abstract void onLeft();
	public abstract void onRight();
}
