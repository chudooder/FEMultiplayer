package net.fe.overworldStage.objective;

import java.util.ArrayList;

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
			if(players.get(i).getParty().size() > 0) {
				if(winner == -1) winner = i;
				else return -1;
			}
		}

		return winner;
	}

	@Override
	public String getDescription() {
		return "KILL EVERYONE";
	}

}
