package net.fe.overworldStage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import chu.engine.Entity;
import net.fe.unit.Item;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;

public class Grid{
	private Unit[][] grid;
	private Terrain[][] terrain;
	public final int width, height;

	public Grid(int width, int height, Terrain defaultTerrain) {
		grid = new Unit[height][width];
		terrain = new Terrain[height][width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				terrain[j][i] = defaultTerrain;
			}
		}
		this.width = width;
		this.height = height;
	}

	/**
	 * Adds a unit to the grid at the given coordinates.
	 * 
	 * @param u
	 * @param x
	 * @param y
	 * @return
	 */
	boolean addUnit(Unit u, int x, int y) {
		if (grid[y][x] != null)
			return false;
		grid[y][x] = u;
		u.xcoord = x;
		u.ycoord = y;
		return true;
	}

	Unit removeUnit(int x, int y) {
		if (grid[y][x] == null)
			return null;
		Unit ans = grid[y][x];
		grid[y][x] = null;
		return ans;
	}
	
	public void move(int x0, int y0, int x1, int y1){
		Unit u = grid[y0][x0];
		grid[y0][x0] = null;
		grid[y1][x1] = u;
	}

	public Terrain getTerrain(int x, int y) {
		return terrain[y][x];
	}
	
	public void setTerrain(int x, int y, Terrain t){
		terrain[y][x] = t;
	}

	public Unit getUnit(int x, int y) {
		return grid[y][x];
	}

	public Path getShortestPath(Unit unit, int x, int y) {
		int move = unit.get("Mov");
		Set<Node> closed = new HashSet<Node>();
		Set<Node> open = new HashSet<Node>();

		Node start = new Node(unit.xcoord, unit.ycoord);
		Node goal = new Node(x, y);
		start.g = 0;
		start.f = heuristic(start, goal);
		open.add(start);

		while (!open.isEmpty()) {
			// get node in open with best f score
			Node cur = null;
			for (Node n : open) {
				if (cur == null || n.f < cur.f) {
					cur = n;
				}
			}
			if (cur.equals(goal)) {
				return getPath(cur);
			}
			
			open.remove(cur);
			closed.add(cur);
			for (Node n : cur.getNeighbors(this)) {
				int g = cur.g
						+ terrain[n.y][n.x].getMoveCost(unit.getTheClass());
				int f = g + heuristic(n, goal);
				if (closed.contains(n) && f >= n.f) {
					continue;
				} else if (!open.contains(n) || f < n.f) {
					if (g > move){
						continue;
					}
					n.parent = cur;
					n.g = g;
					n.f = f;
					open.add(n);
				}
			}
		}

		// failure
		return null;
	}

	public Set<Node> getPossibleMoves(Unit u) {
		int x = u.xcoord;
		int y = u.ycoord;
		Node start = new Node(x,y);
		start.d = 0;
		Set<Node> set = new HashSet<Node>();
		ArrayList<Node> q = new ArrayList<Node>();
		q.add(new Node(x, y));
		
		while(q.size() > 0){
			Node curr = q.remove(0);
			set.add(curr);
			for(Node n: curr.getNeighbors(this)){
				if(!set.contains(n)){
					n.d = curr.d + terrain[y][x].getMoveCost(u.getTheClass());
					if(n.d <= u.get("Mov")){
						q.add(n);
					}
				}
			}
		}
		
		
		return set;
	}
	
	public Set<Node> getAttackRange(Unit u){
		Set<Node> move = getPossibleMoves(u);
		Set<Node> set = new HashSet<Node>();
		Set<Integer> range = new HashSet<Integer>();
		for(Item i: u.getInventory()){
			if(!(i instanceof Weapon)) continue;
			Weapon w = (Weapon) i;
			if(w.type != Weapon.Type.STAFF)
				range.addAll(w.range);
		}
		for(Node n: move){
			for(int i: range){
				set.addAll(getRange(n, i));
			}
		}
		return set;
	}
	
	public Set<Node> getHealRange(Unit u){
		Set<Node> move = getPossibleMoves(u);
		Set<Node> set = new HashSet<Node>();
		Set<Integer> range = new HashSet<Integer>();
		for(Item i: u.getInventory()){
			if(!(i instanceof Weapon)) continue;
			Weapon w = (Weapon) i;
			if(w.type == Weapon.Type.STAFF)
				range.addAll(w.range);
		}
		for(Node n: move){
			for(int i: range){
				set.addAll(getRange(n, i));
			}
		}
		return set;
	}
	
	public Set<Node> getRange(Node start, int range){
		Set<Node> set = new HashSet<Node>();
			for(int dx = -range; dx <=range; dx++){
				for(int dy = -range; dy <= range; dy++){
					Node n = new Node(start.x + dx, start.y + dy);
					if(n.x < 0 || n.x > width-1 ||n.y < 0 || n.y > height-1){
						continue;
					}
					if(n.distance(start) == range && !set.contains(n)){
						set.add(n);
					}
				}
			}
		return set;
	}

	private Path getPath(Node goal) {
		Path path = new Path();
		Node cur = goal;
		do {
			path.add(0,cur);
			cur = cur.parent;
		} while (cur != null);
		System.out.println(path);
		return path;
	}

	private int heuristic(Node a, Node b) {
		// Manhattan heuristic is pretty good because no diag mvmt
		return Math.abs(b.x - a.x) + Math.abs(b.y - a.y);
	}

	public static int getDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	public static int getDistance(Unit a, Unit b) {
		return getDistance(a.xcoord, a.ycoord, b.xcoord, b.ycoord);
	}
}
