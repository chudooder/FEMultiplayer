package net.fe.lobbystage;

import java.util.ArrayList;
import java.util.HashMap;

import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.builderStage.WaitStage;
import net.fe.network.Chat;
import net.fe.network.FEServer;
import net.fe.network.Message;
import net.fe.network.message.ChatMessage;
import net.fe.network.message.ClientInit;
import net.fe.network.message.JoinLobby;
import net.fe.network.message.JoinTeam;
import net.fe.network.message.QuitMessage;
import net.fe.network.message.ReadyMessage;
import net.fe.network.message.StartBuilding;
import net.fe.network.message.StartGame;
import net.fe.overworldStage.OverworldStage;
import chu.engine.Game;
import chu.engine.Stage;

/**
 * Version of LobbyStage used by Server without extraneous entities
 * @author Shawn
 *
 */
public class LobbyStage extends Stage {

	public HashMap<Integer, Player> players;
	protected Chat chat;
	
	public LobbyStage() {
		super("main_theme");
		players = new HashMap<Integer, Player>();
		chat = new Chat();
	}
	@Override
	public void beginStep() {
		for(Message message : Game.getMessages()) {
			if(message instanceof JoinLobby) {
				JoinLobby join = (JoinLobby)message;
				players.put(join.origin, join.player);
			}
			else if(message instanceof JoinTeam) {
				JoinTeam join = (JoinTeam)message;
				players.get(join.origin).setTeam(join.team);
			}
			else if(message instanceof ClientInit) {		// Only clients will get this
				ClientInit init = (ClientInit)message;
				players.putAll(init.players);
			}
			else if(message instanceof QuitMessage) {
				QuitMessage quit = (QuitMessage)message;
				players.remove(quit.origin);
			}
			else if(message instanceof ChatMessage) {
				ChatMessage chatMsg = (ChatMessage)message;
				chat.add(players.get(chatMsg.origin), chatMsg.text);
			}
			else if(message instanceof ReadyMessage) {
				players.get(message.origin).ready = !players.get(message.origin).ready;
				chat.add(players.get(message.origin), "Ready!");
			}
		}
	}

	@Override
	public void onStep() {
		
	}

	@Override
	public void endStep() {
		if(players.size() == 0) return;
		for(Player p : players.values()) {
			if(!p.ready) {
				return;
			}
		}
		// Set up global list of players
		FEServer.players.clear();
		ArrayList<Player> ans = new ArrayList<Player>();
		for(Player p : players.values()) {
			if(p.isSpectator())
				p.getParty().clear();
			ans.add(p);
		}
		FEServer.players.addAll(ans);
		FEServer.getServer().broadcastMessage(new StartBuilding(0));
		FEServer.setCurrentStage(new WaitStage());
	}

}
