package net.fe.overworldStage;

import java.util.HashSet;
import java.util.Set;

public class Node{
	public int x, y, f, g, h, d;
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
	
	public int hashCode(){
		return x+y;
	}
	
	public int distance(Node other){
		return Math.abs(other.x - this.x) + Math.abs(other.y - this.y);
	}
	
	public Set<Node> getNeighbors(Grid g) {
		Set<Node> neighbors = new HashSet<Node>(4);
		if(x < g.width-1)
			neighbors.add(new Node(x+1, y));
		if(y < g.height-1)
			neighbors.add(new Node(x, y+1));
		if(x > 0)
			neighbors.add(new Node(x-1, y));
		if(y > 0)
			neighbors.add(new Node(x, y-1));
		return neighbors;
	}
	
	public String toString(){
		return "(" + x + ", " + y + ")";
	}
}
