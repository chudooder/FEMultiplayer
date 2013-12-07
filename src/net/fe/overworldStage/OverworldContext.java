package net.fe.overworldStage;

import net.fe.unit.Unit;

public abstract class OverworldContext {
	protected OverworldStage stage;
	protected OverworldContext prev;
	protected Cursor cursor;
	protected Grid grid;

	public OverworldContext(OverworldStage s, OverworldContext prevContext) {
		stage = s;
		prev = prevContext;
		cursor = stage.cursor;
		grid = stage.grid;
	}

	public abstract void onSelect();

	public abstract void onCancel();

	public abstract void onUp();
	
	public abstract void onDown();
	
	public abstract void onLeft();
	
	public abstract void onRight();

	protected Unit getHoveredUnit() {
		return stage.getUnit(cursor.xcoord, cursor.ycoord);
	}
}
