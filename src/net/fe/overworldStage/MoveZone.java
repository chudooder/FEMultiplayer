package net.fe.overworldStage;

import java.util.Set;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.GriddedEntity;
import chu.engine.anim.Renderer;

public class MoveZone extends Entity {
	private Set<Node> zone;
	public MoveZone(Set<Node> zone) {
		super(0,0);
		this.zone = zone;
		renderDepth = 0.2f;
	}
	public void render(){
		System.out.println("here");
		for(Node n: zone){
			int x = n.x*17 +1;
			int y = n.y*17 +1;
			Renderer.drawRectangle(x, y, x+16, y+16, 0, new Color(0x00FF0080));
		}
	}
	public Set<Node> getNodes(){
		return zone;
	}
}
