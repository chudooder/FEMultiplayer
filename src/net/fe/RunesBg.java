package net.fe;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;

public class RunesBg extends Entity {
	private Color color;
	private Texture runes;
	private float position;
	public RunesBg(Color c) {
		super(0,0);
		color = c;
		runes = FEResources.getTexture("runes");
		renderDepth = 1;
	}
	
	public void onStep(){
		super.onStep();
		position += Game.getDeltaSeconds() * 20;
		if(position > 480){
			position -= 480;
		}
	}
	
	public void render(){
		Renderer.setColor(color);
		Renderer.render(runes, 0, 0, 2, 1, -position, 0, -position + 480 *2, 320, 1);
		Renderer.setColor(null);
	}

}
