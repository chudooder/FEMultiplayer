package net.fe.overworldStage.objective;

import net.fe.Player;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

public class Rout implements Objective {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4499698067587946141L;

	/**
	 * Returns winner if only one player has remaining units
	 * @param stage
	 * @return
	 */
	@Override
	public int evaluate(OverworldStage stage) {
		int winner = -1;
		for(Player p : stage.getTurnOrder()) {
			boolean ded = true;
			for(Unit u : p.getParty()) {
				if(u.getHp() > 0)
					ded = false;
			}
			if(!ded) {
				if(winner == -1) {
					winner = p.getID();
				}
				else return -1;
			}
		}
		return winner;
	}

	@Override
	public String getDescription() {
		return "Rout the enemy";
	}
	
	@Override
	public String toString() {
		return "Rout";
	}

}
