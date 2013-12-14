package net.fe.overworldStage;

import java.util.ArrayList;

import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.unit.*;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Resources;
import chu.engine.anim.Renderer;
import chu.engine.anim.Sprite;
import chu.engine.anim.Transform;

public class BattlePreview extends Entity {
	private Unit attacker, defender;
	private ArrayList<Sprite> sprites;
	private Sprite leftArrow, rightArrow, x2, x4;
	private int range;

	public BattlePreview(float x, float y, Unit a, Unit d, int range) {
		super(x, y);
		attacker = a;
		defender = d;
		sprites = new ArrayList<Sprite>();
		rightArrow = new Sprite();
		rightArrow.addAnimation("default",
				Resources.getTexture("gui_selectArrow"), 8, 8, 6, 6, 100);
		leftArrow = new Sprite();
		leftArrow.addAnimation("default",
				Resources.getTexture("gui_selectArrow"), 8, 8, 6, 6, 100);
		x2 = new CirclingSprite(2.2f, 1.2f);
		x2.addAnimation("default", Resources.getTexture("x2"));
		x4 = new CirclingSprite(2.2f, 1.2f);
		x4.addAnimation("default", Resources.getTexture("x4"));
		sprites.add(rightArrow);
		sprites.add(leftArrow);
		sprites.add(x2);
		sprites.add(x4);
		this.range = range;
		this.renderDepth = OverworldStage.MENU_DEPTH;
	}

	public void onStep() {
		for (Sprite s : sprites) {
			s.update();
		}
	}

	public void render() {
		String aHit = " - ", aAtk = " - ", aCrit = " - ", dHit = " - ", dAtk = " - ", dCrit = " - ";
		int aMult = 1;
		int dMult = 1;
		String aHp = String.format("%d", attacker.getHp());
		String dHp = String.format("%d", defender.getHp());
		Transform flip = new Transform();
		flip.flipHorizontal();

		if (attacker.getWeapon().range.contains(range)) {
			aHit = String.format(
					"%d",
					Math.max(0,
							Math.min(100, attacker.hit() - defender.avoid())));
			aAtk = String.format("%d",
					CombatCalculator.calculateBaseDamage(attacker, defender));
			aCrit = String.format(
					"%d",
					Math.max(0,
							Math.min(100, attacker.crit() - defender.avoid())));
			if(attacker.get("Spd") >= defender.get("Spd") + 4) aMult*=2;
			if(attacker.getWeapon().name.contains("Brave")) aMult*=2;
			
		}
		if (defender.getWeapon().range.contains(range)) {
			dHit = String.format(
					"%d",
					Math.max(0,
							Math.min(100, defender.hit() - attacker.avoid())));
			dAtk = String.format("%d",
					CombatCalculator.calculateBaseDamage(defender, attacker));
			dCrit = String.format(
					"%d",
					Math.max(0,
							Math.min(100, defender.crit() - attacker.avoid())));
			if(defender.get("Spd") >= attacker.get("Spd") + 4) dMult*=2;
			if(defender.getWeapon().name.contains("Brave")) dMult*=2;
		}

		// Borders
		Renderer.drawRectangle(x - 1, y - 1, x + 91, y + 132, renderDepth,
				FightStage.BORDER_DARK);
		Renderer.drawRectangle(x, y, x + 90, y + 35, renderDepth,
				FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x + 59, y + 35, x + 90, y + 96, renderDepth,
				FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x, y + 35, x + 31, y + 96, renderDepth,
				FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x, y + 96, x + 90, y + 131, renderDepth,
				FightStage.BORDER_LIGHT);

		// Top item and name
		Renderer.drawRectangle(x + 1, y + 1, x + 89, y + 34, renderDepth,
				attacker.getPartyColor());
		Renderer.drawString("default_med", attacker.name, x + 2, y + 2,
				renderDepth);
		new ItemDisplay((int) x + 7, (int) y + 16, attacker.getWeapon())
				.render();
		leftArrow.render(x, y + 21, renderDepth);
		rightArrow.renderTransformed(x + 90, y + 21, renderDepth, flip);

		// Attacker stats

		Renderer.drawRectangle(x + 60, y + 34, x + 89, y + 96, renderDepth,
				attacker.getPartyColor());
		Renderer.drawString("default_med", aHp, x + 63, y + 36, renderDepth);
		Renderer.drawString("default_med", aAtk, x + 63, y + 51, renderDepth);
		Renderer.drawString("default_med", aHit, x + 63, y + 66, renderDepth);
		Renderer.drawString("default_med", aCrit, x + 63, y + 81, renderDepth);
		if(aMult == 2){
			x2.render(x+77, y+49, renderDepth);
		} else if (aMult == 4){
			x4.render(x+77, y+49, renderDepth);
		}

		// Stats text
		Renderer.drawRectangle(x + 31, y + 35, x + 59, y + 96, renderDepth,
				FightStage.NEUTRAL);
		Renderer.drawString("default_med", "HP", x + 37, y + 36, renderDepth);
		Renderer.drawString("default_med", "Atk", x + 36, y + 51, renderDepth);
		Renderer.drawString("default_med", "Hit", x + 36, y + 66, renderDepth);
		Renderer.drawString("default_med", "Crt", x + 36, y + 81, renderDepth);

		// Defender stats
		Renderer.drawRectangle(x + 1, y + 35, x + 30, y + 97, renderDepth,
				defender.getPartyColor());
		Renderer.drawString("default_med", dHp, x + 4, y + 36, renderDepth);
		Renderer.drawString("default_med", dAtk, x + 4, y + 51, renderDepth);
		Renderer.drawString("default_med", dHit, x + 4, y + 66, renderDepth);
		Renderer.drawString("default_med", dCrit, x + 4, y + 81, renderDepth);
		if(dMult == 2){
			x2.render(x+18, y+49, renderDepth);
		} else if (dMult == 4){
			x4.render(x+18, y+49, renderDepth);
		}

		// Bottom item and name
		Renderer.drawRectangle(x+1, y + 97, x + 89, y + 130, renderDepth,
				defender.getPartyColor());
		Renderer.drawString("default_med", defender.name, x + 2, y + 98,
				renderDepth);
		new ItemDisplay((int) x + 7, (int) y + 112, defender.getWeapon())
				.render();
	}
	
	private class CirclingSprite extends Sprite {
		private float timer;
		private float radius;
		private float period;
		public CirclingSprite(float radius, float period) {
			timer = 0;
			this.radius = radius;
			this.period = period;
		}
		
		public void update() {
			super.update();
			timer += Game.getDeltaSeconds();
			if(timer > period) timer -= period;
		}
		
		public void render(float x, float y, float depth) {
			double radians = timer/period*2*Math.PI;
			super.render((float)(x+radius*Math.cos(radians)), (float)(y+radius*Math.sin(radians)), depth);
		}
	}
}
