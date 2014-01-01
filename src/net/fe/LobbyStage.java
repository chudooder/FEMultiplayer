package net.fe;

import static net.fe.fightStage.FightStage.NEUTRAL;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

/**
 * Lobby where the players wait before the game.
 * @author Shawn
 *
 */
public class LobbyStage extends Stage {
	
	ArrayList<Player> unassigned;
	ArrayList<Player> spectators;
	ArrayList<Player> competitors;
	
	public static final Color BORDER_DARK = new Color(0x483828);
	public static final Color BORDER_LIGHT = new Color(0xf8f0c8);
	public static final Color NEUTRAL = new Color(0xb0a878);
	public static final Color NEUTRAL_DARK = NEUTRAL.darker(0.5f);
	
	public LobbyStage() {
		competitors = new ArrayList<Player>();
		spectators = new ArrayList<Player>();
		unassigned = new ArrayList<Player>();
	}

	@Override
	public void beginStep() {
		
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render() {
		// Draw boxes
		Renderer.drawRectangle(0, 0, 480, 320, 1.0f, NEUTRAL);
		Renderer.drawRectangle(1, 1, 479, 319, 1.0f, BORDER_DARK);
		Renderer.drawRectangle(2, 2, 478, 318, 1.0f, BORDER_LIGHT);
		Renderer.drawRectangle(3, 3, 477, 317, 1.0f, NEUTRAL);
		int x, y;
		x = 6;
		y = 22;
		Renderer.drawString("default_med", "Players", x, y-14, 0.9f);
		Renderer.drawRectangle(x, y, x+300, y+164, 1.0f, NEUTRAL_DARK);
		y = y + 164 + 16;
		Renderer.drawString("default_med", "Chat", x, y-14, 0.9f);
		Renderer.drawRectangle(x, y, x+300, 314, 1.0f, NEUTRAL_DARK);
		y = 22;
		x = x + 300 + 16;
		Renderer.drawString("default_med", "Unassigned Players", x, y-14, 0.9f);
		Renderer.drawRectangle(x, y, 474, y+164, 1.0f, NEUTRAL_DARK);
		y = y + 164 + 16;
		Renderer.drawString("default_med", "Game info", x, y-14, 0.9f);
		Renderer.drawRectangle(x, y, 474, 314, 1.0f, NEUTRAL_DARK);
	}

}
