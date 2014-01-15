package net.fe;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearDepth;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class LoadStage{
	private static float percent;
	private static int max;

	public static void setMaximum(int max){
		LoadStage.max = max;
	}
	public static void update(int number){
		percent = (number + 0f)/max;
	}

	public static void render(){
		int width = (int) (percent * 436);
		glClear(GL_COLOR_BUFFER_BIT |
		        GL_DEPTH_BUFFER_BIT |
		        GL_STENCIL_BUFFER_BIT);
		glClearDepth(1.0f);
		Renderer.drawString("default_med", "FE: Multiplayer is loading...", 22, 263, 0);
		String percentText = (int)(percent * 100) + "%";
		int pwidth = FEResources.getBitmapFont("default_med").getStringWidth(percentText);
		Renderer.drawString("default_med", percentText, 458 - pwidth, 262, 0);
		Renderer.drawRectangle(20, 280, 460, 300, 0, Color.gray);
		Renderer.drawRectangle(22, 282, 22+width, 298, 0, Color.blue.darker());
		Display.update();
	}

}
