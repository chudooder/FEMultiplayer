package chu.engine;

public class GriddedEntity extends Entity {

	protected int xcoord;
	protected int ycoord;
	
	public GriddedEntity() {
		this(0, 0);
	}

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
	
	public int getXCoord(){
		return xcoord;
	}
	
	public int getYCoord(){
		return ycoord;
	}
	
	//ONLY THE GRID SHOULD CALL THESE METHODS.
	public void gridSetXCoord(int x){
		xcoord = x;
	}
	
	public void gridSetYCoord(int y){
		ycoord = y;
	}
}
