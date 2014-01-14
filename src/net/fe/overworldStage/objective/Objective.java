package net.fe.overworldStage.objective;

import net.fe.overworldStage.OverworldStage;

public interface Objective {
	/**
	 * Returns the client ID of the winner, 
	 * or -1 if there is no winner
	 * @param stage
	 * @return
	 */
	public int evaluate(OverworldStage stage);
	public String getDescription();
}
