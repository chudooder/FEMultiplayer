package chu.engine;

public class GriddedEntity extends Entity {

	public int xcoord;
	public int ycoord;

	public GriddedEntity(int xx, int yy) {
		super(xx * 17 + 1, yy * 17 + 1);
		xcoord = xx;
		ycoord = yy;
	}

	@Override
	public void beginStep() {
		x = xcoord * 17 + 1;
		y = ycoord * 17 + 1;
	}

	@Override
	public void endStep() {

	}
}
