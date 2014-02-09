package net.fe.overworldStage;

import java.util.List;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.network.Chat;
import net.fe.network.message.ChatMessage;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;
import chu.engine.menu.TextInputBox;

public class OverworldChat extends TextInputBox implements DoNotDestroy {
	
	private Chat chat;
	private static final Color TEXTBOX = new Color(0.2f, 0.2f, 0.2f, 0.7f);
	private static final Color CURSOR = new Color(1f, 1f, 1f, 1f);
	
	public OverworldChat(Chat chat) {
		super(280,200,200,20,"default_med");
		this.chat = chat;
		renderDepth = ClientOverworldStage.CHAT_DEPTH;
	}
	
	public void render() {
		BitmapFont font = FEResources.getBitmapFont("default_med");
		if(hasFocus) {
			Renderer.drawRectangle(x, y, x+width, y+height, renderDepth, TEXTBOX);
			float linepos = x + font.getStringWidth(input.substring(0, cursorPos)) + 2;
			Renderer.drawRectangle(linepos, y+1, linepos+1, y+height-1, renderDepth-0.02f, CURSOR);
			Renderer.drawString("default_med", input.toString(), x+2, y+5, renderDepth-0.01f);
		}
		List<String> chats = chat.getLast(5);
		for(int i=0; i<5; i++) {
			Renderer.drawString("default_med", chats.get(i), 
					Game.getWindowWidth()-2-font.getStringWidth(chats.get(i)), y-82+i*16, renderDepth);
		}
	}
	
	public void beginStep() {
		super.beginStep();
		List<KeyboardEvent> keys = Game.getKeys();
		for(KeyboardEvent ke : keys) {
			if(ke.state) {
				if(ke.key == Keyboard.KEY_RETURN) { 
					if(hasFocus) {
						send();
						((ClientOverworldStage)stage).setControl(true);
						hasFocus = false;
					} else {
						hasFocus = true;
						((ClientOverworldStage)stage).setControl(false);
					}
				}
			}
		}
	}
	
	public void send() {
		if(input.length() == 0) return;
		byte id = FEMultiplayer.getClient().getID();
		FEMultiplayer.getClient().sendMessage(
				new ChatMessage(id, input.toString()));
		input.delete(0, input.length());
		cursorPos = 0;
	}

}
