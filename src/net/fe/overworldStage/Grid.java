package net.fe.overworldStage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.newdawn.slick.Color;

import net.fe.Party;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

public class Grid{
	private Unit[][] grid;
	private Terrain[][] terrain;
	private int blueThroneX, blueThroneY;
	private int redThroneX, redThroneY;
	public final int width, height;

	public Grid(int width, int height, Terrain defaultTerrain) {
		grid = new Unit[height][width];
		blueThroneX = -1;
		blueThroneY = -1;
		redThroneX = -1;
		redThroneY = -1;
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
	public boolean addUnit(Unit u, int x, int y) {
		if (grid[y][x] != null)
			return false;
		grid[y][x] = u;
		u.gridSetXCoord(x);
		u.gridSetYCoord(y);
		u.setOrigX(x);
		u.setOrigY(y);
		return true;
	}
	
	public Unit findUnit(UnitIdentifier u){
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				if(u.equals(new UnitIdentifier(getUnit(i,j)))){
					return getUnit(i,j);
				}
			}
		}
		return null;
	}

	Unit removeUnit(int x, int y) {
		if (grid[y][x] == null)
			return null;
		Unit ans = grid[y][x];
		grid[y][x] = null;
		return ans;
	}
	
	public void move(Unit u, int x, int y, boolean animated){
		grid[u.getYCoord()][u.getXCoord()] = null;
		grid[y][x] = u;
		if(!animated){
			u.gridSetXCoord(x);
			u.gridSetYCoord(y);
		}
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
	
	public void setThronePos(Color c, int x, int y) {
		if(c.equals(Party.TEAM_BLUE)) {
			blueThroneX = x;
			blueThroneY = y;
			System.out.println("Blue throne: "+x+" "+y);
		} else {
			redThroneX = x;
			redThroneY = y;
			System.out.println("Red throne: "+x+" "+y);
		}
	}
	
	public boolean canSeize(Unit u) {
		//check if lord
		if(!u.getTheClass().name.equals("Lord"))
			return false;
		Color c = u.getPartyColor();
		if(c.equals(Party.TEAM_BLUE) 
				&& u.getXCoord() == redThroneX
				&& u.getYCoord() == redThroneY) {
			return true;
		} else if (c.equals(Party.TEAM_RED)
				&& u.getXCoord() == blueThroneX
				&& u.getYCoord() == blueThroneY){
			return true;
		}
		return false;
	}

	public Path getShortestPath(Unit unit, int x, int y) {
		int move = unit.get("Mov");
		if(grid[y][x] != null && grid[y][x] != unit) return null;
		Set<Node> closed = new HashSet<Node>();
		Set<Node> open = new HashSet<Node>();

		Node start = new Node(unit.getXCoord(), unit.getYCoord());
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
				for(Node o : open) {
					if(o.equals(n)) n = o;
				}
				int g = cur.g
						+ terrain[n.y][n.x].getMoveCost(unit.getTheClass());
				if(grid[n.y][n.x] != null && grid[n.y][n.x].getParty() != unit.getParty()) {
					g += 128;
				}
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
		int x = u.getXCoord();
		int y = u.getYCoord();
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
					n.d = curr.d + terrain[n.y][n.x].getMoveCost(u.getTheClass());
					if(grid[n.y][n.x] != null && grid[n.y][n.x].getParty() != u.getParty()) {
						n.d += 128;
					}
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
		Set<Integer> range = u.getTotalWepRange(false);
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
		Set<Integer> range = u.getTotalWepRange(true);
		for(Node n: move){
			set.addAll(getRange(n, range));
		}
		return set;
	}
	
	public Set<Node> getRange(Node start, Collection<Integer> range){
		int[] r = new int[range.size()];
		Iterator<Integer> it = range.iterator();
		for(int i = 0; i < range.size(); i++){
			r[i] = it.next();
		}
		return getRange(start, r);
	}
	
	public Set<Node> getRange(Node start, int... range){
		Set<Node> set = new HashSet<Node>();
		for(int r: range){
			for(int dx = -r; dx <=r; dx++){
				for(int dy = -r; dy <= r; dy++){
					Node n = new Node(start.x + dx, start.y + dy);
					if(n.x < 0 || n.x > width-1 ||n.y < 0 || n.y > height-1){
						continue;
					}
					if(n.distance(start) == r && !set.contains(n)){
						set.add(n);
					}
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
		return getDistance(a.getXCoord(), a.getYCoord(), b.getXCoord(), b.getYCoord());
	}
}
