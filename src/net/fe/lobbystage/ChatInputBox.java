package net.fe.lobbystage;

import java.util.HashMap;
import java.util.List;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.network.message.ChatMessage;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.MouseEvent;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;

public class ChatInputBox extends Entity {
	
	private int width;
	private int height;
	private boolean hasFocus;
	private int cursorPos;
	private StringBuilder input;
	
	private static final Color UNFOCUSED = new Color(0x58543c);
	private static final Color FOCUSED = new Color(0x817b58);
	private static final Color CURSOR = new Color(0xeeeeee);

	public ChatInputBox() {
		super(6, 294);
		width = 250;
		height = 20;
		hasFocus = false;
		cursorPos = 0;
		renderDepth = 0.9f;
		input = new StringBuilder();
	}
	
	public void beginStep() {
		List<MouseEvent> mouseEvents = Game.getMouseEvents();
		for(MouseEvent event : mouseEvents) {
			if(event.button == 0) {
				if(inBounds(event.x, Game.getWindowHeight()-event.y)) {
					hasFocus = true;
				} else {
					hasFocus = false;
				}
			}
		}
		if(hasFocus) {
			List<KeyboardEvent> keys = Game.getKeys();
			for(KeyboardEvent ke : keys) {
				if(ke.state) {
					char c = ke.eventChar;
					if(isValidCharacter(c) && FEResources.getBitmapFont("default_med")
							.getStringWidth(input.toString()+c) < 246) {
						input.insert(cursorPos, c);
						cursorPos++;
					} else {
						if(ke.key == Keyboard.KEY_LEFT && cursorPos > 0) { 
							cursorPos--;
						}
						else if(ke.key == Keyboard.KEY_RIGHT && cursorPos < input.length()) { 
							cursorPos++;
						}
						else if(ke.key == Keyboard.KEY_BACK && cursorPos > 0) { 
							input.deleteCharAt(cursorPos-1);
							cursorPos--;
						}
						else if(ke.key == Keyboard.KEY_RETURN && input.length() > 0) { 
							byte id = FEMultiplayer.getClient().getID();
							FEMultiplayer.getClient().sendMessage(
									new ChatMessage(id, input.toString()));
							input.delete(0, input.length());
							cursorPos = 0;
						}
					}
				}
			}
		}
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

	private boolean inBounds(float otherX, float otherY) {
		if(otherX < x || otherX >= x+width 
				|| otherY < y || otherY >= y+height) {
			return false;
		}
		return true;
	}
	
	private boolean isValidCharacter(char c) {
		return FEResources.getBitmapFont("default_med").containsCharacter(c);
	}

}
