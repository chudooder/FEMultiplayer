package net.fe.lobbystage;

import java.util.List;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.network.message.ChatMessage;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.MouseEvent;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;
import chu.engine.menu.TextInputBox;

public class LobbyChatBox extends TextInputBox {
	
	private static final Color UNFOCUSED = new Color(0x58543c);
	private static final Color FOCUSED = new Color(0x817b58);
	private static final Color CURSOR = new Color(0xeeeeee);

	public LobbyChatBox() {
		super(6, 294, 250, 20, "default_med");
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
		super.beginStep();
		if(hasFocus) {
			List<KeyboardEvent> keys = Game.getKeys();
			for(KeyboardEvent ke : keys) {
				if(ke.state) {
					if(ke.key == Keyboard.KEY_RETURN) { 
						send();
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

	public void send() {
		if(input.length() == 0) return;
		AudioPlayer.playAudio("cancel", 1, 1);
		byte id = FEMultiplayer.getClient().getID();
		FEMultiplayer.getClient().sendMessage(
				new ChatMessage(id, input.toString()));
		input.delete(0, input.length());
		cursorPos = 0;
	}

}
