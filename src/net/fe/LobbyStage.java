package net.fe;

import static net.fe.fightStage.FightStage.NEUTRAL;

import java.util.ArrayList;

import net.fe.network.Message;
import net.fe.network.ServerLobby;
import net.fe.network.message.JoinLobby;
import net.fe.network.message.QuitMessage;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.Renderer;
import chu.engine.menu.MenuButton;

/**
 * Lobby where the players wait before the game.
 * @author Shawn
 *
 */
public class LobbyStage extends ServerLobby {
	
	public static final Color BORDER_DARK = new Color(0x483828);
	public static final Color BORDER_LIGHT = new Color(0xf8f0c8);
	public static final Color NEUTRAL = new Color(0xb0a878);
	public static final Color NEUTRAL_DARK = NEUTRAL.darker(0.5f);
	
	public LobbyStage() {
		super();
		MenuButton spectateButton = new MenuButton(409, 22, 64, 32) {
			{
				sprite.addAnimation("default", FEResources.getTexture("spectate_button"));
			}
			@Override
			public void onClick() {
				FEMultiplayer.getLocalPlayer().joinTeam(Player.TEAM_SPECTATOR);
			}
		};
		MenuButton playButton = new MenuButton(409, 57, 64, 32) {
			{
				sprite.addAnimation("default", FEResources.getTexture("play_button"));
			}
			@Override
			public void onClick() {
				FEMultiplayer.getLocalPlayer().joinTeam(Player.TEAM_RED);
			}
		};
		MenuButton unassignButton = new MenuButton(409, 92, 64, 32) {
			{
				sprite.addAnimation("default", FEResources.getTexture("unassign_button"));
			}
			@Override
			public void onClick() {
				FEMultiplayer.getLocalPlayer().joinTeam(Player.TEAM_UNASSIGNED);
			}
		};
		//TODO: Exit game
		MenuButton exitButton = new MenuButton(409, 127, 64, 32) {
			{
				sprite.addAnimation("default", FEResources.getTexture("exit_button"));
			}
			@Override
			public void onClick() {
				FEMultiplayer.getClient().quit();
			}
		};
		addEntity(spectateButton);
		addEntity(playButton);
		addEntity(unassignButton);
		addEntity(exitButton);
	}

	@Override
	public void beginStep() {
		super.beginStep();
		for(Entity e : entities) {
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
	}

	@Override
	public void onStep() {
		super.onStep();
		for(Entity e : entities) {
			e.onStep();
		}
		processAddStack();
		processRemoveStack();
	}

	@Override
	public void endStep() {
		super.endStep();
		for(Entity e : entities) {
			e.endStep();
		}
		processAddStack();
		processRemoveStack();
	}
	
	@Override
	public void render() {
		super.render();
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
		Renderer.drawRectangle(x, y, x+84, y+74, 1.0f, NEUTRAL_DARK);
		y = y + 75 + 16;
		Renderer.drawString("default_med", "Spectators", x, y-14, 0.9f);
		Renderer.drawRectangle(x, y, x+84, y+74, 1.0f, NEUTRAL_DARK);
		y = y + 75 + 16;
		Renderer.drawString("default_med", "Game info", x, y-14, 0.9f);
		Renderer.drawRectangle(x, y, 474, 314, 1.0f, NEUTRAL_DARK);
		
		int a = 0;
		int b = 0;
		int c = 0;
		for(Player p : players.values()) {
			if(p.getTeam() == Player.TEAM_UNASSIGNED) {
				Renderer.drawString("default_med", p.getName(), 324, 27+(a++)*16, 0.8f);
			} else if(p.getTeam() == Player.TEAM_SPECTATOR) {
				Renderer.drawString("default_med", p.getName(), 324, 115+(b++)*16, 0.8f);
			} else {
				Renderer.drawString("default_med", p.getName(), 8, 24+(c++)*16, 0.8f);
			}
		}
	}
}
