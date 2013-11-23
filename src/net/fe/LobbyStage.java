package net.fe;

import java.util.ArrayList;

import chu.engine.Stage;

/**
 * Lobby where the players wait before the game.
 * @author Shawn
 *
 */
public class LobbyStage extends Stage {
	
	ArrayList<Player> unassigned;
	ArrayList<Player> spectators;
	ArrayList<Player> competitors;
	
	public LobbyStage() {
		competitors = new ArrayList<Player>();
		spectators = new ArrayList<Player>();
		unassigned = new ArrayList<Player>();
	}

	@Override
	public void beginStep() {
		
		
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub
		
	}

}
