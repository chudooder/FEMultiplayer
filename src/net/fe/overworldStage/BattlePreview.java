package net.fe.overworldStage;

import java.util.ArrayList;

import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.unit.*;
import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.anim.Renderer;
import chu.engine.anim.Sprite;
import chu.engine.anim.Transform;

public class BattlePreview extends Entity {
	private Unit attacker, defender;
	private ArrayList<Sprite> sprites;
	private Sprite leftArrow, rightArrow;
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
		sprites.add(rightArrow);
		sprites.add(leftArrow);
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

		// Bottom item and name
		Renderer.drawRectangle(x+1, y + 97, x + 89, y + 130, renderDepth,
				defender.getPartyColor());
		Renderer.drawString("default_med", defender.name, x + 2, y + 98,
				renderDepth);
		new ItemDisplay((int) x + 7, (int) y + 112, defender.getWeapon())
				.render();
	}
}
