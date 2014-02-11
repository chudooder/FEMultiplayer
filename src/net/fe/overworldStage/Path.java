package net.fe.overworldStage;

import java.util.Iterator;
import java.util.LinkedList;

import net.fe.FEResources;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;

public class Path extends Entity{
	private LinkedList<Node> path;
	private static Tileset arrows;
	
	static {
		if(Game.glContextExists())
			arrows = new Tileset(FEResources.getTexture("move_arrows"), 16, 16);
	}
	
	public Path() {
		super(0,0);
		path = new LinkedList<Node>();
		renderDepth = ClientOverworldStage.PATH_DEPTH;

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
		ClientOverworldStage cs = (ClientOverworldStage)stage;
		Renderer.translate(-cs.camX, -cs.camY);
		Renderer.addClip(0, 0, 368, 240, true);
		
		Iterator<Node> it = path.iterator();
		Node cur = it.next();
		Node next = it.hasNext() ? it.next() : null;
		Node prev = null;
		for(int i=0; i<path.size(); i++){
			int tx = 0;
			int ty = 0;
			if(prev == null) {
				if(next == null) {
					tx = -1;
					ty = -1;
				} else {
					ty = 0;
					if(next.x == cur.x+1) tx = 0;
					else if(next.y == cur.y-1) tx = 1;
					else if(next.x == cur.x-1) tx = 2;
					else if(next.y == cur.y+1) tx = 3;
				}
			} else {
				if(next == null) {
					ty = 3;
					if(cur.x == prev.x+1) tx = 0;
					else if(cur.y == prev.y-1) tx = 1;
					else if(cur.x == prev.x-1) tx = 2;
					else if(cur.y == prev.y+1) tx = 3;
				} else {
					if(prev.x == next.x || prev.y == next.y) { 
						ty = 1;
						if(next.x == cur.x+1) tx = 0;
						else if(next.y == cur.y-1) tx = 1;
						else if(next.x == cur.x-1) tx = 2;
						else if(next.y == cur.y+1) tx = 3;
					} else {
						ty = 2;
						if(next.x == cur.x+1 && cur.y == prev.y-1
								|| next.y == cur.y+1 && cur.x == prev.x-1) tx = 0;
						else if(next.y == cur.y-1 && cur.x == prev.x-1
								|| next.x == cur.x+1 && cur.y == prev.y+1) tx = 1;
						else if(next.x == cur.x-1 && cur.y == prev.y+1
								|| next.y == cur.y-1 && cur.x == prev.x+1) tx = 2;
						else if(next.y == cur.y+1 && cur.x == prev.x+1
								|| next.x == cur.x-1 && cur.y == prev.y-1) tx = 3;
					}
				}
			}
			

			if(tx != -1 && ty != -1)
				arrows.render(cur.x*16, cur.y*16, tx, ty, renderDepth);
			
			prev = cur;
			cur = next;
			next = it.hasNext() ? it.next() : null;
		}
		
		Renderer.removeClip();
		Renderer.translate(cs.camX, cs.camY);
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

	public Node[] getAllNodes() {
		Node[] nodes = new Node[path.size()];
		path.toArray(nodes);
		return nodes;
	}
}