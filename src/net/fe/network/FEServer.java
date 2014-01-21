package net.fe.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
	public static LobbyStage lobby;
	public static ArrayList<Player> players;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("FEServer");
		try {
			frame.add(new JLabel("Server IP: " + InetAddress.getLocalHost().getHostAddress()){{
				this.setFont(getFont().deriveFont(20f));
				this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			}});
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
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
		lobby = new LobbyStage();
		currentStage = lobby;
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

	public static void resetToLobby() {
		for(Player p : players) {
			p.ready = false;
		}
		currentStage = lobby;
	}

}
