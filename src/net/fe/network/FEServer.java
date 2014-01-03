package net.fe.network;

import java.util.ArrayList;

import net.fe.FEResources;
import net.fe.LobbyStage;
import chu.engine.Game;
import chu.engine.Stage;

/**
 * A game that does not render anything. Manages logic only
 * @author Shawn
 *
 */
public class FEServer extends Game {
	
	private Server server;
	private static Stage currentStage;
	
	public static void main(String[] args) {
		FEServer feserver = new FEServer();
		feserver.init();
		feserver.loop();
	}
	
	public FEServer() {
		server = new Server();
		Thread serverThread = new Thread() {
			public void run() {
				server.start(21255);
			}
		};
		currentStage = new ServerLobby();
		serverThread.start();
	}
	
	public void init() {
		messages = new ArrayList<Message>();
	}
	
	@Override
	public void loop() {
		boolean yes = true;
		while(yes) {
			time = System.nanoTime();
			messages.clear();
			messages.addAll(server.messages);
			for(Message m : messages)
				server.messages.remove(m);
			currentStage.beginStep();
			currentStage.onStep();
			currentStage.endStep();
			timeDelta = System.nanoTime()-time;
		}
	}
	
	public static Stage getCurrentStage() {
		return currentStage;
	}

}
