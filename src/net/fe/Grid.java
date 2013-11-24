package net.fe;

public class Grid {
	private GriddedEntity[][] grid;
	private Terrain[][] terrain;
	
	public Grid(int width, int height) {
		grid = new GriddedEntity[height][width];
		terrain = new Terrain[height][width];
	}

	/**
	 * Adds a unit to the grid at the given coordinates.
	 * @param u
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean addUnit(Unit u, int x, int y) {
		if(grid[y][x] != null)
			return false;
		grid[y][x] = u;
		u.xcoord = x;
		u.ycoord = y;
		return true;
	}
	
	public boolean removeUnit(int x, int y) {
		if(grid[y][x] == null)
			return false;
		grid[y][x] = null;
		return true;
	}
	
}
