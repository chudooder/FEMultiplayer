package net.fe.transition;

import net.fe.FEMultiplayer;
import net.fe.Transition;
import net.fe.fightStage.FightStage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Grid;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.anim.Renderer;

public class FightOverworldTransition extends Transition {
	ClientOverworldStage to;
	private float timer;
	private float triAlpha;
	private float fightAlpha;
	private static final float LENGTH = .6666f;
	
	private float[] x;
	private float[] y;
	private float[] dx;
	private float[] dy;
	/**
	 * x0 ... y1 are the coords for the two tiles to fight over
	 */
	public FightOverworldTransition(ClientOverworldStage to, UnitIdentifier u1, UnitIdentifier u2) {
		this(to, FEMultiplayer.getUnit(u1), FEMultiplayer.getUnit(u2));
	}
	
	public FightOverworldTransition(ClientOverworldStage to, Unit a, Unit b) {
		super(to);
		this.to = to;
		to.beginStep();
		renderDepth = 0.0f;
		triAlpha = 0f;
		fightAlpha = 0.0f;
		float x0 = a.getXCoord() - to.camX/16;
		float y0 = a.getYCoord() - to.camY/16;
		float x1 = b.getXCoord() - to.camX/16;
		float y1 = b.getYCoord() - to.camY/16;
		int range = Grid.getDistance(a, b);
		float[] destX= new float[] {x0, x0+1, x0+1, x0, x1, x1+1, x1+1, x1};
		float[] destY = new float[] {y0, y0, y0+1, y0+1, y1, y1, y1+1, y1+1};
		for(int i=0; i<8; i++) {
			destX[i] *= 8;
			destY[i] *= 8;
		}
		if(range == 1) {
			x = new float[] {FightStage.CENTRAL_AXIS-56, FightStage.CENTRAL_AXIS,
				FightStage.CENTRAL_AXIS, FightStage.CENTRAL_AXIS-77,
				FightStage.CENTRAL_AXIS, FightStage.CENTRAL_AXIS+56,
				FightStage.CENTRAL_AXIS+77, FightStage.CENTRAL_AXIS};
		} else {
			x = new float[] {FightStage.CENTRAL_AXIS-76, FightStage.CENTRAL_AXIS-21,
				FightStage.CENTRAL_AXIS-29, FightStage.CENTRAL_AXIS-105,
				FightStage.CENTRAL_AXIS+21, FightStage.CENTRAL_AXIS+76,
				FightStage.CENTRAL_AXIS+105, FightStage.CENTRAL_AXIS+29};
		}
		y = new float[] {FightStage.FLOOR-16, FightStage.FLOOR-16,
				FightStage.FLOOR+8, FightStage.FLOOR+8,
				FightStage.FLOOR-16, FightStage.FLOOR-16,
				FightStage.FLOOR+8, FightStage.FLOOR+8,};
		dx = new float[8];
		dy = new float[8];
		for(int i=0; i<8; i++) {
			dx[i] = (destX[i] - x[i])*3f;
			dy[i] = (destY[i] - y[i])*3f;
		}
		timer = 0;
	}
	
	public void render() {
		float delta = Game.getDeltaSeconds();
		// Reverse the scaling of the fight stage
		Renderer.pushMatrix();
		Renderer.scale(0.5f, 0.5f);
		// Render overworld stage (with transparency)
		Renderer.drawRectangle(0, 0, Game.getWindowWidth(), Game.getWindowHeight(), 0.0f, 
				new Color(0.0f, 0.0f, 0.0f, Math.min(1.0f, timer*5)));
		Renderer.setColor(new Color(1f, 1f, 1f, fightAlpha));
		if(timer > 0.4) fightAlpha += 6*delta;
		to.render();
		Renderer.setColor(null);
		Renderer.popMatrix();
		// Render terrain zoom thing
		Color triColor = new Color(1.0f, 1.0f, 1.0f, triAlpha);
		for(int i=0; i<3; i++) {
			Renderer.drawTriangle(x[0], y[0], x[i], y[i], x[i+1], y[i+1], 0.0f, triColor);
			Renderer.drawTriangle(x[4], y[4], x[i+4], y[i+4], x[i+5], y[i+5], 0.0f, triColor);
		}
		if(timer < 0.3333) {
			triAlpha += 0.7f * 3 * delta;
		} else {
			for(int i=0; i<8; i++) {
				x[i] += dx[i]*delta;
				y[i] += dy[i]*delta;
			}
		}
		timer += delta;
		if(timer > LENGTH) done();
	}
	
	@Override
	public void done() {
		super.done();
		to.playSoundTrack();
		to.checkEndGame();
	}
	
}
