package net.fe;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class Button extends Entity{
	protected String text;
	private Color color;
	private boolean hover;
	private int width;
	public Button(float x, float y, String text, Color color, int width) {
		super(x, y);
		this.text = text;
		this.color = color;
		this.width = width;
	}
		
	public void render(){
		int stringWidth = FEResources.getBitmapFont("default_med").getStringWidth(text);
		Color c = new Color(color);
		if(!hover)
			c = c.darker();
		Renderer.drawRectangle(x, y, x+width, y+20, renderDepth, c);
		Renderer.drawString("default_med", text, x+width/2-stringWidth/2 + 2, y + 4, renderDepth);
		
	}
	
	public void setHover(boolean hover){
		this.hover = hover;
	}
	
	public boolean hovered(){
		return hover;
	}
}
