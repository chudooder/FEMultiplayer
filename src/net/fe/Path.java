package net.fe;

import java.util.LinkedList;

public class Path {
	private LinkedList<Node> path;
	public Path() {
		path = new LinkedList<Node>();
	}
	
	public void add(Node n) {
		path.add(n);
	}
	
	public void add(int pos, Node n) {
		path.add(pos, n);
	}
	
	public void render() {
		//TODO: Draw an arrow
	}
}