package net.fe.overworldStage;

import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.GriddedEntity;
import chu.engine.anim.Renderer;

public class Zone extends Entity {
	private Set<Node> zone;
	private Color color;
	
	public static Color MOVE_DARK = new Color(0xC04444FF);
	public static Color ATTACK_DARK = new Color(0xC0FF4444);
	public static Color HEAL_DARK = new Color(0xC044FF44);
	
	public static Color MOVE_LIGHT = new Color(0xC08888FF);
	public static Color ATTACK_LIGHT = new Color(0xC0FF8888);
	public static Color HEAL_LIGHT = new Color(0xC088FF88);
	public Zone(Set<Node> zone, Color c) {
		super(0,0);
		this.zone = zone;
		this.color = c;
		renderDepth = OverworldStage.ZONE_DEPTH;
	}
	public void render(){
		for(Node n: zone){
			int x = n.x*16;
			int y = n.y*16;
			Renderer.drawRectangle(x, y, x+16, y+16, renderDepth, color);
		}
	}
	public Set<Node> getNodes(){
		return zone;
	}
	public static Zone minus(Zone a, Zone b){
		Set<Node> nodes = new HashSet<Node>(a.getNodes());
		nodes.removeAll(b.getNodes());
		return new Zone(nodes, a.color);
	}
}
