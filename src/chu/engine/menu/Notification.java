package chu.engine.menu;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;

public class Notification extends Entity {

	private String text;
	private Transform transform;
	private String font;
	private float timer;
	private float lifetime;
	
	public Notification(float x, float y, String font, String text, float lifetime, float depth) {
		this(x, y, font, text, lifetime, Color.white, depth);
	}
	
	public Notification(float x, float y, String font, String text, float lifetime, Color c, float depth) {
		super(x, y);
		this.font = font;
		this.text = text;
		this.lifetime = lifetime;
		this.timer = 0f;
		renderDepth = depth;
		transform = new Transform();
		transform.color = c;
	}
	
	public void endStep() {
		timer += Game.getDeltaSeconds();
		if(timer > lifetime) destroy();
	}
	
	public void render() {
		Renderer.drawString(font, text, x, y, renderDepth, transform);
	}

}
