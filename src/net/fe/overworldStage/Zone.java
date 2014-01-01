package net.fe.overworldStage;

import java.util.HashSet;
import java.util.Set;

import net.fe.FEResources;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;
import chu.engine.anim.Transform;

public class Zone extends Entity {
	private Set<Node> zone;
	private Color color;
	
	public static Color MOVE_DARK = new Color(0xC04444FF);
	public static Color ATTACK_DARK = new Color(0xC0FF4444);
	public static Color HEAL_DARK = new Color(0xC044FF44);
	
	public static Color MOVE_LIGHT = new Color(0xC08888FF);
	public static Color ATTACK_LIGHT = new Color(0xC0FF8888);
	public static Color HEAL_LIGHT = new Color(0xC088FF88);
	
	private static int frame;
	private static float timer;
	private static Tileset tiles;
	
	public Zone(Set<Node> zone, Color c) {
		super(0,0);
		this.zone = zone;
		this.color = c;
		frame = 0;
		tiles = new Tileset(FEResources.getTexture("zone_colors"), 15, 15);
		renderDepth = OverworldStage.ZONE_DEPTH;
	}
	public void render(){
		for(Node n: zone){
			int x = n.x*16;
			int y = n.y*16;
			Renderer.drawRectangle(x, y, x+16, y+16, renderDepth, color);
			Color mult;
			if(color == MOVE_DARK || color == ATTACK_DARK || color == HEAL_DARK)
				mult = new Color(1f, 1f, 1f, 0.5f);
			else
				mult = new Color(1f, 1f, 1f, .75f);
			Transform t = new Transform();
			t.setColor(mult.multiply(color));
			if(color == MOVE_DARK || color == MOVE_LIGHT) {
				tiles.renderTransformed(x, y, frame, 0, renderDepth, t);
			} else if(color == ATTACK_DARK || color == ATTACK_LIGHT) {
				tiles.renderTransformed(x, y, frame, 1, renderDepth, t);
			} else if(color == HEAL_DARK || color == HEAL_LIGHT) {
				tiles.renderTransformed(x, y, frame, 2, renderDepth, t);
			}
		}
	}
	
	public void beginStep() {
		timer += Game.getDeltaSeconds();
		if(timer > 0.3f) {
			frame++;
			timer = 0;
			if(frame >= 16) frame = 0;
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
