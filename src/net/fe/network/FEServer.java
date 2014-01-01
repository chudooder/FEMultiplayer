package net.fe.network;

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
	private Stage currentStage;
	
	public static void main(String[] args) {
		FEServer feserver = new FEServer();
		feserver.loop();
	}
	
	public FEServer() {
		Thread serverThread = new Thread() {
			public void run() {
				server = new Server(21255);
			}
		};
		currentStage = new LobbyStage();
		serverThread.start();
	}
	
	@Override
	public void init(int width, int height, String name) {
		// don't do anything
	}
	
	@Override
	public void loop() {
		boolean yes = true;
		while(yes) {
			time = System.nanoTime();
			currentStage.beginStep();
			currentStage.onStep();
			currentStage.endStep();
			timeDelta = System.nanoTime()-time;
		}
	}

}
