package net.fe;

import java.util.ArrayList;

public class Node{
	int x, y, f, g, h;
	Node parent;
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof Node)) return false;
		Node node = (Node) other;
		if(this.x == node.x && this.y == node.y)
			return true;
		return false;
	}
	
	public ArrayList<Node> getNeighbors() {
		ArrayList<Node> neighbors = new ArrayList<Node>();
		neighbors.add(new Node(x+1, y));
		neighbors.add(new Node(x, y+1));
		neighbors.add(new Node(x-1, y));
		neighbors.add(new Node(x, y-1));
		return neighbors;
	}
}
