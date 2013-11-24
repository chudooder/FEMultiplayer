package net.fe;

import chu.engine.Game;
import chu.engine.network.Server;
import chu.engine.Stage;

/**
 * A game that does not render anything. Manages logic only
 * @author Shawn
 *
 */
public class FEServer extends Game {
	
	private static Server server;
	private static Stage currentStage;
	
	public static void main(String[] args) {
		server = new Server(21255);
	}
	
	@Override
	public void init(int width, int height, String name) {
		// don't do anything
	}
	
	@Override
	public void loop() {
		while(server != null) {
			time = System.nanoTime();
			currentStage.beginStep();
			currentStage.onStep();
			currentStage.endStep();
			timeDelta = System.nanoTime()-time;
		}
	}

}
