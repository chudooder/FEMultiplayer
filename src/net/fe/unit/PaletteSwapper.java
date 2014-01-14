package net.fe.unit;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import org.lwjgl.opengl.GL13;
import org.newdawn.slick.opengl.Texture;

import chu.engine.anim.Renderer;

public class PaletteSwapper {
	
	private Texture palette;
	
	static {
		Renderer.addProgram("paletteSwap", "paletteSwap", "paletteSwap");
	}
	
	public PaletteSwapper(Texture palette) {
		this.palette = palette;
	}
	
	public void setUpPalette() {
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, palette.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}
}
