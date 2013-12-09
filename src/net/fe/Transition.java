package net.fe;

import chu.engine.Entity;
import chu.engine.Stage;

/**
 * Manages a delayed transition between two stages.
 * @author Shawn
 *
 */
public abstract class Transition extends Entity {
	
	Stage from, to;
	
	public Transition(Stage from, Stage to) {
		super(0,0);
		this.from = from;
		this.to = to;
	}
	
	public void done() {
		destroy();
		FEMultiplayer.setCurrentStage(to);
	}
}
