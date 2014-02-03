package net.fe.overworldStage.objective;

import net.fe.Player;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

public class Seize implements Objective {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1608192440668201886L;
	@Override
	public int evaluate(OverworldStage stage) {
		
		// If a player has a Lord on the other player's throne, they win
		// Alternatively, if a player's Lord dies, they lose
		byte winner = -1;
		for(Player p : stage.getNonSpectators()) {
			boolean hasLord = false;
			for(int i=0; i<p.getParty().size(); i++) {
				Unit u = p.getParty().getUnit(i);
				if(u.getTheClass().name.equals("Lord") && u.getHp() > 0) {
					hasLord = true;
					System.out.println(p.getName()+" has a Lord!");
				}
				if(stage.grid.canSeize(u)) {
					return p.getID();
				}
			}
			if(hasLord) {
				if(winner == -1) {
					winner = p.getID();
				} else {
					winner = -2;
				}
			}
		}
		if(winner > 0) {
			System.out.println(winner+" has a Lord and wins!");
			return winner;
		}
		else return -1;
	}
;
	@Override
	public String getDescription() {
		return "Seize the throne";
	}
	
	@Override
	public String toString() {
		return "Seize";
	}

}
