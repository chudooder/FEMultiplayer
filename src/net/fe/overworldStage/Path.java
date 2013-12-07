package net.fe.overworldStage;

import java.util.LinkedList;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class Path extends Entity{
	private LinkedList<Node> path;
	public Path() {
		super(0,0);
		path = new LinkedList<Node>();
		renderDepth = OverworldStage.PATH_DEPTH;
	}
	
	public void add(Node n) {
		path.add(n);
	}
	
	public void add(int pos, Node n) {
		path.add(pos, n);
	}
	
	public Node removeFirst(){
		return path.removeFirst();
	}
	
	public Node getFirst(){
		return path.getFirst();
	}
	
	public int size(){
		return path.size();
	}
	
	public void render() {
		int x = path.getFirst().x;
		int y = path.getFirst().y;
		for(Node n: path){
			int newX = n.x;
			int newY = n.y;
			Renderer.drawLine(x*17+9, y*17+9, newX*17+9, newY*17+9, 5, 0, Color.white, Color.white);
			x = newX;
			y = newY;
		}
	}
	
	public Path getCopy(){
		Path copy = new Path();
		for(Node n : path){
			copy.add(n);
		}
		return copy;
	}
	
	public String toString(){
		return path.toString();
	}
}