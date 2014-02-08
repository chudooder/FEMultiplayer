package net.fe.transition;

import org.newdawn.slick.Color;

import net.fe.FEResources;
import net.fe.Transition;
import net.fe.overworldStage.EndGameStage;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;

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
		String string = winner+" is the winner!";
		int width = FEResources.getBitmapFont("default_med").getStringWidth(string);
		Transform t = new Transform();
		t.scaleX = 2;
		t.scaleY = 2;
		t.color = Color.green;
		Renderer.drawString("default_med", string, 240-width, 130, 0.1f, t);
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
