package net.fe.lobbystage;

import java.util.ArrayList;
import java.util.HashMap;

import net.fe.Player;
import net.fe.network.Chat;
import net.fe.network.Message;
import net.fe.network.message.ChatMessage;
import net.fe.network.message.ClientInit;
import net.fe.network.message.JoinLobby;
import net.fe.network.message.JoinTeam;
import net.fe.network.message.QuitMessage;
import chu.engine.Game;
import chu.engine.Stage;

/**
 * Version of LobbyStage used by Server without extraneous entities
 * @author Shawn
 *
 */
public class ServerLobby extends Stage {

	public HashMap<Integer, Player> players;
	protected Chat chat;
	
	public ServerLobby() {
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
		}
	}

	@Override
	public void onStep() {
		
	}

	@Override
	public void endStep() {
		
	}

}
