package net.fe.overworldStage;

public abstract class OverworldContext {
	protected OverworldStage stage;
	public OverworldContext(OverworldStage s){
		stage = s;
	}
	public abstract void onSelect();
	public abstract void onCancel();
	public abstract void onUp();
	public abstract void onDown();
	public abstract void onLeft();
	public abstract void onRight();
}
