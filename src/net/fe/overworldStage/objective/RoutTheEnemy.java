package net.fe.overworldStage.objective;

import java.util.ArrayList;

import net.fe.Party;
import net.fe.Player;
import net.fe.overworldStage.OverworldStage;

public class RoutTheEnemy implements Objective {

	/**
	 * Returns winner if only one player has remaining units
	 * @param stage
	 * @return
	 */
	@Override
	public int evaluate(OverworldStage stage) {
		ArrayList<Player> players = stage.getNonSpectators();
		int winner = -1;
		for(int i=0; i<players.size(); i++) {
			Party party = players.get(i).getParty();
			boolean ded = true;
			for(int j=0; j<party.size(); j++) {
				if(party.getUnit(j).getHp() > 0)
					ded = false;
			}
			System.out.print("|");
			if(!ded) {
				if(winner == -1) winner = players.get(i).getID();
				else return -1;
			}
		}
		
		return winner;
	}

	@Override
	public String getDescription() {
		return "Rout";
	}

}
