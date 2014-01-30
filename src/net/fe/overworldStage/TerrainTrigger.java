package net.fe.overworldStage;

import net.fe.Player;
import chu.engine.Entity;

public abstract class TerrainTrigger {
	public final boolean start;
	public TerrainTrigger(boolean startTurn){
		start = startTurn;
	}
	public boolean attempt(OverworldStage g, int x, int y, Player turnPlayer){
		return false;
	}
	public void startOfTurn(OverworldStage g, int x, int y){
		
	}
	
	public void endOfTurn(OverworldStage g, int x, int y){
		
	}
	
	public Entity getAnimation(OverworldStage g, int x, int y){
		return null;
	}
}
