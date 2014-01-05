package net.fe.network;

import java.util.ArrayList;

import net.fe.FEResources;
import net.fe.Player;
import net.fe.lobbystage.ClientLobbyStage;
import net.fe.lobbystage.LobbyStage;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import chu.engine.Game;
import chu.engine.Stage;

/**
 * A game that does not render anything. Manages logic only
 * @author Shawn
 *
 */
public class FEServer extends Game {
	
	private static Server server;
	private static Stage currentStage;
	public static ArrayList<Player> players;
	
	public static void main(String[] args) {
		FEServer feserver = new FEServer();
		feserver.init();
		feserver.loop();
	}
	
	public FEServer() {
		players = new ArrayList<Player>();
		server = new Server();
		Thread serverThread = new Thread() {
			public void run() {
				server.start(21255);
			}
		};
		currentStage = new LobbyStage();
		serverThread.start();
	}
	
	public void init() {
		messages = new ArrayList<Message>();
	}
	
	public static Unit getUnit(UnitIdentifier id){
		for(Player p: players){
			if(!p.isSpectator() && p.getParty().getColor().equals(id.partyColor)){
				return p.getParty().search(id.name);
			}
		}
		return null;
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
	
	public static void setCurrentStage(Stage stage) {
		currentStage = stage;
	}

	public static Server getServer() {
		return server;
	}

}
