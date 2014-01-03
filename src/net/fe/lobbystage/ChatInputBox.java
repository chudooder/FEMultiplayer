package net.fe.lobbystage;

import net.fe.FEResources;

import org.newdawn.slick.Color;

import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;
import chu.engine.menu.TextInputBox;

public class ChatInputBox extends TextInputBox {
	
	private static final Color UNFOCUSED = new Color(0x58543c);
	private static final Color FOCUSED = new Color(0x817b58);
	private static final Color CURSOR = new Color(0xeeeeee);

	public ChatInputBox() {
		super(6, 294, 250, 20, "default_med");
	}
	
	public void render() {
		BitmapFont font = FEResources.getBitmapFont("default_med");
		if(hasFocus) {
			Renderer.drawRectangle(x, y, x+width, y+height, renderDepth, FOCUSED);
			float linepos = x + font.getStringWidth(input.substring(0, cursorPos)) + 2;
			Renderer.drawRectangle(linepos, y+1, linepos+1, y+height-1, renderDepth-0.02f, CURSOR);
		} else {
			Renderer.drawRectangle(x, y, x+width, y+height, renderDepth, UNFOCUSED);
		}
		Renderer.drawString("default_med", input.toString(), x+2, y+5, renderDepth-0.01f);
	}

}
