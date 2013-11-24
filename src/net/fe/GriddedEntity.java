package net.fe;
import chu.engine.Entity;

public class GriddedEntity extends Entity {

	public int xcoord;
	public int ycoord;

	public GriddedEntity(int xx, int yy) {
		super(xx * 16, yy * 16);
		xcoord = xx;
		ycoord = yy;
	}

	@Override
	public void beginStep() {
		x = xcoord * 16;
		y = ycoord * 16;
	}

	@Override
	public void endStep() {

	}
}
