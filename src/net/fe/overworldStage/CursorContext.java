package net.fe.overworldStage;

public abstract class CursorContext extends OverworldContext {
	public CursorContext(OverworldStage s, OverworldContext prevContext) {
		super(s, prevContext);
		// TODO Auto-generated constructor stub
	}
	
	public void onUp() {
		if (cursor.ycoord > 0) {
			cursor.ycoord--;
			cursorChanged();
		}
	}

	public void onDown() {
		if (cursor.ycoord < grid.height - 1) {
			cursor.ycoord++;
			cursorChanged();
		}
	}

	public void onLeft() {
		if (cursor.xcoord > 0) {
			cursor.xcoord--;
			cursorChanged();
		}

	}

	public void onRight() {
		if (cursor.xcoord < grid.width - 1) {
			cursor.xcoord++;
			cursorChanged();
		}
	}

	public abstract void cursorChanged();

}
