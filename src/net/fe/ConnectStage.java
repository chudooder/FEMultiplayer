package net.fe;

import java.util.List;

import net.fe.builderStage.TeamBuilderStage;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.MouseEvent;
import chu.engine.Stage;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;
import chu.engine.menu.MenuButton;
import chu.engine.menu.TextInputBox;

/**
 * Player selects name and server ip
 * @author Shawn
 *
 */
public class ConnectStage extends Stage {
	
	public static final Color BORDER_DARK = new Color(0x483828);
	public static final Color BORDER_LIGHT = new Color(0xf8f0c8);
	public static final Color NEUTRAL = new Color(0xb0a878);
	public static final Color NEUTRAL_DARK = new Color(0x58543c);
	
	private static final Color UNFOCUSED = new Color(0x58543c);
	private static final Color FOCUSED = new Color(0x817b58);
	private static final Color CURSOR = new Color(0xeeeeee);
	
	private ConnectInputBox name;
	private ConnectInputBox ip;
	
	public ConnectStage() {
		super("main_theme");
		name = new ConnectInputBox(180,136,153,20);
		ip = new ConnectInputBox(180,166,100,20);
		addEntity(name);
		addEntity(ip);
		addEntity(new ConnectButton(286,166,47,20));
		addEntity(new MenuButton(180,196,128,32) {
			{
				sprite.addAnimation("default", FEResources.getTexture("team_builder_button"));
			}
			@Override
			public void onClick() {
				FEMultiplayer.setCurrentStage(new TeamBuilderStage(true));
			}
			@Override
			public void render() {
				if(hover) {
					sprite.render(x, y, renderDepth, null, "lighten");
				} else {
					sprite.render(x, y, renderDepth);
				}
			}
		});
		processAddStack();
	}

	@Override
	public void beginStep() {
		for(Entity e : entities) {
			e.beginStep();
		}
	}

	@Override
	public void onStep() {
		for(Entity e : entities) {
			e.onStep();
		}
	}

	@Override
	public void endStep() {
		for(Entity e : entities) {
			e.endStep();
		}
	}
	
	public void render() {
		// Draw and label boxes
		Renderer.drawRectangle(0, 0, 480, 320, 1.0f, NEUTRAL);
		Renderer.drawRectangle(1, 1, 479, 319, 1.0f, BORDER_DARK);
		Renderer.drawRectangle(2, 2, 478, 318, 1.0f, BORDER_LIGHT);
		Renderer.drawRectangle(3, 3, 477, 317, 1.0f, NEUTRAL);
		
		Renderer.drawString("default_med", "Name:", 150, 140, 0.0f);
		Renderer.drawString("default_med", "Server IP:", 133, 170, 0.0f);
		
		super.render();
	}
	
	private class ConnectInputBox extends TextInputBox {
		public ConnectInputBox(int x, int y, int w, int h) {
			super(x, y, w, h, "default_med");
			renderDepth = 0.0f;
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
	
	private class ConnectButton extends MenuButton {

		public ConnectButton(float x, float y, float w, float h) {
			super(x, y, w, h);
			sprite.addAnimation("default", FEResources.getTexture("connect_button"));
		}
		
		@Override
		public void onClick() {
			AudioPlayer.playAudio("select", 1, 1);
			FEMultiplayer.connect(name.getInput(), ip.getInput());
		}
		@Override
		public void render() {
			if(hover) {
				sprite.render(x, y, renderDepth, null, "lighten");
			} else {
				sprite.render(x, y, renderDepth);
			}
		}
		
	}

}
