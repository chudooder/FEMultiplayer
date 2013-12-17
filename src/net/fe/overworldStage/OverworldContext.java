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

	public void onCancel(){
		prev.startContext();
	}

	public abstract void onUp();
	
	public abstract void onDown();
	
	public abstract void onLeft();
	
	public abstract void onRight();
	
	public void startContext(){
		stage.setContext(this);
	}
	
	public void cleanUp(){
		
	}

	protected Unit getHoveredUnit() {
		return stage.getUnit(cursor.getXCoord(), cursor.getYCoord());
	}
}
