package net.fe.network;

import java.util.ArrayList;
import java.util.HashMap;

import net.fe.Player;
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

	protected HashMap<Integer, Player> players;
	
	public ServerLobby() {
		players = new HashMap<Integer, Player>();
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
		}
	}

	@Override
	public void onStep() {
		
	}

	@Override
	public void endStep() {
		
	}

}
