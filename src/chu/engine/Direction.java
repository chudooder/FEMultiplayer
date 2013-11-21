package chu.engine;

public enum Direction {
	SOUTHWEST (-16, 16, 180.0f), 
	SOUTH (0, 16, 270.0f), 
	SOUTHEAST (16, 16, 270.0f), 
	WEST (-16, 0, 180.0f), 
	NONE (0, 0, 0.0f), 
	EAST (16, 0, 0.0f), 
	NORTHWEST (-16, -16, 90.0f), 
	NORTH (0, -16, 90.0f), 
	NORTHEAST (16, -16, 0.0f);
	
	private final int dx;
	private final int dy;
	private final float angle;
	
	Direction(int x, int y, float f) {
		dx = x;
		dy = y;
		angle = f;
	}
	
	public int getX() {
		return dx;
	}
	
	public int getY() {
		return dy;
	}
	
	public int getUnitX() {
		return dx/16;
	}
	
	public int getUnitY() {
		return dy/16;
	}
	
	
	public float getAngle() {
		return angle;
	}
	
	
	public boolean isDiagonal() {
		if(this == NORTHEAST || this == SOUTHEAST || this == NORTHWEST || this == SOUTHWEST)
			return true;
		return false;
	}
	
	public static Direction[] getAdjacentDirections(Direction d) {
		Direction[] temp = new Direction[8];
		Direction[] adjacent = new Direction[3];
		temp[0] = SOUTHWEST;
		temp[1] = SOUTH;
		temp[2] = SOUTHEAST;
		temp[3] = EAST;
		temp[4] = NORTHEAST;
		temp[5] = NORTH;
		temp[6] = NORTHWEST;
		temp[7] = WEST;
		for(int i=0; i<8; i++) {
			if(temp[i] == d) {
				adjacent[0] = temp[(i+7)%8];
				adjacent[1] = d;
				adjacent[2] = temp[(i+9)%8];
				return adjacent;
			}
		}
		return null;
	}
	
	public static Direction getOppositeDirection(Direction d) {
		switch(d) {
		case SOUTHWEST : return NORTHEAST;
		case SOUTH : return NORTH;
		case SOUTHEAST : return NORTHWEST;
		case WEST : return EAST;
		case NONE : return NONE;
		case EAST : return WEST;
		case NORTHWEST : return SOUTHEAST;
		case NORTH : return SOUTH;
		case NORTHEAST : return NORTHWEST;
		}
		return NONE;
	}
	
}
