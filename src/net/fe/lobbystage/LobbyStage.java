package net.fe.lobbystage;

import net.fe.Player;
import net.fe.Session;
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
import chu.engine.Game;
import chu.engine.Stage;

/**
 * Version of LobbyStage used by Server without extraneous entities
 * @author Shawn
 *
 */
public class LobbyStage extends Stage {
	protected Chat chat;
	protected Session session;
	
	public LobbyStage(Session s) {
		super("main_theme");
		session = s;
		chat = new Chat();
	}
	@Override
	public void beginStep() {
		for(Message message : Game.getMessages()) {
			if(message instanceof JoinLobby) {
				JoinLobby join = (JoinLobby)message;
				session.addPlayer((byte) join.origin, join.player);
			}
			else if(message instanceof JoinTeam) {
				JoinTeam join = (JoinTeam)message;
				session.getPlayer(join.origin).setTeam(join.team);
				session.getPlayer(join.origin).ready = false;
			}
			else if(message instanceof ClientInit) {		// Only clients will get this
				ClientInit init = (ClientInit)message;
				session = init.session;
			}
			else if(message instanceof QuitMessage) {
				QuitMessage quit = (QuitMessage)message;
				session.removePlayer(quit.origin);
			}
			else if(message instanceof ChatMessage) {
				ChatMessage chatMsg = (ChatMessage)message;
				chat.add(session.getPlayer(chatMsg.origin), chatMsg.text);
			}
			else if(message instanceof ReadyMessage) {
				if(session.getPlayer(message.origin).getTeam() == Player.TEAM_UNASSIGNED)
					continue;
				boolean ready = !session.getPlayer(message.origin).ready;
				session.getPlayer(message.origin).ready = ready;
				if(ready)
					chat.add(session.getPlayer(message.origin), "Ready!");
				else
					chat.add(session.getPlayer(message.origin), "Not ready!");
			}
		}
	}

	@Override
	public void onStep() {
		
	}

	@Override
	public void endStep() {
		if(session.numPlayers() == 0) return;
		for(Player p : session.getPlayers()) {
			if(!p.ready) {
				return;
			}
		}
		FEServer.getServer().broadcastMessage(new StartBuilding((byte)0));
		FEServer.setCurrentStage(new WaitStage(session));
	}

}
