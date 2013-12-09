package net.fe.overworldStage;

public abstract class CursorContext extends OverworldContext {
	public CursorContext(OverworldStage s, OverworldContext prevContext) {
		super(s, prevContext);
		// TODO Auto-generated constructor stub
	}
	
	public void onUp() {
		if (cursor.getYCoord() > 0) {
			cursor.setYCoord(cursor.getYCoord() - 1);
			cursorChanged();
		}
	}

	public void onDown() {
		if (cursor.getYCoord() < grid.height - 1) {
			cursor.setYCoord(cursor.getYCoord() + 1);
			cursorChanged();
		}
	}

	public void onLeft() {
		if (cursor.getXCoord() > 0) {
			cursor.setXCoord(cursor.getXCoord() - 1);
			cursorChanged();
		}

	}

	public void onRight() {
		if (cursor.getXCoord() < grid.width - 1) {
			cursor.setXCoord(cursor.getXCoord() + 1);
			cursorChanged();
		}
	}

	public abstract void cursorChanged();

}
