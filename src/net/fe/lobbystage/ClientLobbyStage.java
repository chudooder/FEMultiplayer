package net.fe.lobbystage;

import java.util.List;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.Player;
import net.fe.Session;
import net.fe.builderStage.ClientWaitStage;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.modifier.Modifier;
import net.fe.network.Message;
import net.fe.network.message.ReadyMessage;
import net.fe.network.message.StartBuilding;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;
import chu.engine.menu.MenuButton;

/**
 * Lobby where the players wait before the game.
 * @author Shawn
 *
 */
public class ClientLobbyStage extends LobbyStage {
	
	public static final Color BORDER_DARK = new Color(0x483828);
	public static final Color BORDER_LIGHT = new Color(0xf8f0c8);
	public static final Color NEUTRAL = new Color(0xb0a878);
	public static final Color NEUTRAL_DARK = new Color(0x58543c);
	
	private LobbyChatBox chatInput;
	
	public ClientLobbyStage(Session session) {
		super(session);
		chatInput = new LobbyChatBox();
		MenuButton spectateButton = new MenuButton(409, 22, 64, 32) {
			{
				sprite.addAnimation("default", FEResources.getTexture("spectate_button"));
			}
			@Override
			public void onClick() {
				AudioPlayer.playAudio("select", 1, 1);
				FEMultiplayer.getLocalPlayer().joinTeam(Player.TEAM_SPECTATOR);
			}
			@Override
			public void render() {
				if(hover) {
					sprite.render(x, y, renderDepth, null, "lighten");
				} else {
					sprite.render(x, y, renderDepth);
				}
			}
		};
		MenuButton playButton = new MenuButton(409, 57, 64, 32) {
			{
				sprite.addAnimation("default", FEResources.getTexture("play_button"));
			}
			@Override
			public void onClick() {
				AudioPlayer.playAudio("select", 1, 1);
				FEMultiplayer.getLocalPlayer().joinTeam(Player.TEAM_PLAYERS);
			}
			@Override
			public void render() {
				if(hover) {
					sprite.render(x, y, renderDepth, null, "lighten");
				} else {
					sprite.render(x, y, renderDepth);
				}
			}
		};
		MenuButton unassignButton = new MenuButton(409, 92, 64, 32) {
			{
				sprite.addAnimation("default", FEResources.getTexture("unassign_button"));
			}
			@Override
			public void onClick() {
				AudioPlayer.playAudio("select", 1, 1);
				FEMultiplayer.getLocalPlayer().joinTeam(Player.TEAM_UNASSIGNED);
			}
			@Override
			public void render() {
				if(hover) {
					sprite.render(x, y, renderDepth, null, "lighten");
				} else {
					sprite.render(x, y, renderDepth);
				}
			}
		};
		MenuButton exitButton = new MenuButton(409, 127, 64, 32) {
			{
				sprite.addAnimation("default", FEResources.getTexture("exit_button"));
			}
			@Override
			public void onClick() {
				AudioPlayer.playAudio("select", 1, 1);
				FEMultiplayer.getClient().quit();
			}
			@Override
			public void render() {
				if(hover) {
					sprite.render(x, y, renderDepth, null, "lighten");
				} else {
					sprite.render(x, y, renderDepth);
				}
			}
		};
		MenuButton sendButton = new MenuButton(259, 294, 47, 20) {
			{
				sprite.addAnimation("default", FEResources.getTexture("send_button"));
			}
			@Override
			public void onClick() {
				chatInput.send();
			}
			@Override
			public void render() {
				if(hover) {
					sprite.render(x, y, renderDepth, null, "lighten");
				} else {
					sprite.render(x, y, renderDepth);
				}
			}
		};
		MenuButton readyButton = new MenuButton(409, 162, 64, 32) {
			{
				sprite.addAnimation("default", FEResources.getTexture("ready_button"));
			}
			@Override
			public void onClick() {
				AudioPlayer.playAudio("select", 1, 1);
				FEMultiplayer.getClient().sendMessage(new ReadyMessage());
			}
			@Override
			public void render() {
				if(hover) {
					sprite.render(x, y, renderDepth, null, "lighten");
				} else {
					sprite.render(x, y, renderDepth);
				}
			}
		};
		addEntity(spectateButton);
		addEntity(playButton);
		addEntity(unassignButton);
		addEntity(exitButton);
		addEntity(sendButton);
		addEntity(readyButton);
		addEntity(chatInput);
	}

	@Override
	public void beginStep() {
		super.beginStep();
		for(Entity e : entities) {
			e.beginStep();
		}
		for(Message message : Game.getMessages()) {
			if(message instanceof StartBuilding) {
				// Set up global list of players
				for(Player p : FEMultiplayer.getPlayers().values()) {
					if(p.equals(FEMultiplayer.getLocalPlayer()))
						FEMultiplayer.setLocalPlayer(p);
					if(p.isSpectator())
						p.getParty().clear();
				}
				if(!FEMultiplayer.getLocalPlayer().isSpectator()) {
					TeamBuilderStage stage = new TeamBuilderStage(false, session);
					FEMultiplayer.setCurrentStage(stage);
				} else {
					ClientWaitStage stage = new ClientWaitStage(session);
					FEMultiplayer.setCurrentStage(stage);
				}
			}
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
		for(Entity e : entities) {
			e.endStep();
		}
		processAddStack();
		processRemoveStack();
	}
	
	@Override
	public void render() {
		super.render();
		// Draw and label boxes
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
		Renderer.drawRectangle(x, y, x+300, y+89, 1.0f, NEUTRAL_DARK);
		y = 22;
		x = x + 300 + 16;
		Renderer.drawString("default_med", "Unassigned Players", x, y-14, 0.9f);
		Renderer.drawRectangle(x, y, x+84, y+74, 1.0f, NEUTRAL_DARK);
		y = y + 75 + 16;
		Renderer.drawString("default_med", "Spectators", x, y-14, 0.9f);
		Renderer.drawRectangle(x, y, x+84, y+74, 1.0f, NEUTRAL_DARK);
		y = y + 75 + 16;
		Renderer.drawString("default_med", "Game info", x, y-14, 0.9f);
		Renderer.drawString("default_med", "Map: "+session.getMap(), x+2, y+2, 0.9f);
		Renderer.drawString("default_med", "Objective: "+session.getObjective().getDescription(), x+2, y+16, 0.9f);
		Renderer.drawString("default_med", "Modifiers: ", x+2, y+30, 0.9f);
		int yy = 0;
		for(Modifier m : session.getModifiers()) {
			Renderer.drawString("default_med", "* "+m.getName(), x+20, y+44+yy*14, 0.9f);
			yy++;
		}
		Renderer.drawRectangle(x, y, 474, 314, 1.0f, NEUTRAL_DARK);
		
		// Draw players in correct locations
		int a = 0;
		int b = 0;
		int c = 0;
		final int tightSpacing = 16;
		for(Player p : session.getPlayers()) {
			Transform t = new Transform();
			if(p.ready) {
				t.setColor(new Color(90,200,90));
			}
			if(p.getTeam() == Player.TEAM_UNASSIGNED) {
				Renderer.drawString("default_med", p.getName(), 324, 27+(a++)*tightSpacing, 0.8f, t);
			} else if(p.getTeam() == Player.TEAM_SPECTATOR) {
				Renderer.drawString("default_med", p.getName(), 324, 115+(b++)*tightSpacing, 0.8f, t);
			} else {
				Renderer.drawString("default_med", p.getName(), 8, 24+(c++)*tightSpacing, 0.8f, t);

			}
		}
		
		//Draw chat
		x = 6;
		y = 202;
		List<String> chats = chat.getLast(5);
		for(int i=0; i<5; i++) {
			Renderer.drawString("default_med", chats.get(i), x+2, y+2+i*16, 0.8f);
		}
	}
}
