package net.fe.overworldStage;

import net.fe.FEMultiplayer;
import net.fe.unit.Unit;

public class StunTrigger extends TerrainTrigger{
	public void startOfTurn(Grid g, int x, int y){
		
		Unit u = g.getUnit(x, y);
		if(FEMultiplayer.turn.getParty() == u.getParty()){
			u.setMoved(true);
			g.getTerrain(x, y).removeTrigger(this);
		}
		
	}
}
