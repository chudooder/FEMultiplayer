package net.fe;
import chu.engine.Entity;

public class GriddedEntity extends Entity {

	public int xcoord;
	public int ycoord;

	public GriddedEntity(int xx, int yy) {
		super(xx * 32, yy * 32);
		xcoord = xx;
		ycoord = yy;
	}

	@Override
	public void beginStep() {
		x = xcoord * 32;
		y = ycoord * 32;
	}

	@Override
	public void endStep() {

	}
}
