package net.fe.overworldStage;

public abstract class CursorContext extends OverworldContext {
	public CursorContext(OverworldStage s, OverworldContext prevContext) {
		super(s, prevContext);
	}
	
	public void onUp() {
		if (cursor.getYCoord() > 0) {
			cursorWillChange();
			cursor.setYCoord(cursor.getYCoord() - 1);
			cursorChanged();
		}
	}

	public void onDown() {
		if (cursor.getYCoord() < grid.height - 1) {
			cursorWillChange();
			cursor.setYCoord(cursor.getYCoord() + 1);
			cursorChanged();
		}
	}

	public void onLeft() {
		if (cursor.getXCoord() > 0) {
			cursorWillChange();
			cursor.setXCoord(cursor.getXCoord() - 1);
			cursorChanged();
		}

	}

	public void onRight() {
		if (cursor.getXCoord() < grid.width - 1) {
			cursorWillChange();
			cursor.setXCoord(cursor.getXCoord() + 1);
			cursorChanged();
		}
	}

	public abstract void cursorChanged();
	public abstract void cursorWillChange();
}
