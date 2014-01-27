package net.fe.transition;

import net.fe.Transition;
import net.fe.overworldStage.EndGameStage;
import chu.engine.Game;
import chu.engine.anim.Renderer;

public class OverworldEndTransition extends Transition{
	
	private float timer;
	private float endPos;
	private String winner;

	public OverworldEndTransition(EndGameStage to, String winner) {
		super(to);
		timer = 0.0f;
		endPos = -320;
		this.winner = winner;
	}
	
	public void render() {
		Renderer.drawString("default_med", winner+" wins!", 250, 150, 0.1f);
		timer += Game.getDeltaSeconds();
		if(timer > 3.0f) {
			Renderer.translate(0,endPos);
			to.render();
			Renderer.translate(0, -endPos);
			endPos += Game.getDeltaSeconds()*600;
			if(endPos > 0) done();
		}
	}

}
