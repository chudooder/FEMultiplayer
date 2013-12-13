package net.fe.overworldStage;

import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.unit.*;
import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class BattlePreview extends Entity {
	private Unit attacker, defender;
	private int range;

	public BattlePreview(float x, float y, Unit a, Unit d, int range) {
		super(x, y);
		attacker = a;
		defender = d;
		this.range = range;
		this.renderDepth = OverworldStage.MENU_DEPTH;
	}

	public void render() {
		String aHit = " - ", aAtk = " - ", aCrit = " - ", dHit = " - ", dAtk = " - ", dCrit = " - ";
		String aHp = String.format("%d", attacker.getHp());
		String dHp = String.format("%d", defender.getHp());
		if (attacker.getWeapon().range.contains(range)) {
			aHit = String.format("%d", Math.max(0, Math.min(100, 
					attacker.hit() - defender.avoid())));
			aAtk = String.format("%d",
					CombatCalculator.calculateBaseDamage(attacker, defender));
			aCrit = String.format("%d", Math.max(0, Math.min(100, 
					attacker.crit() - defender.avoid())));
		}
		if (defender.getWeapon().range.contains(range)) {
			dHit = String.format("%d", Math.max(0, Math.min(100, 
					defender.hit() - attacker.avoid())));
			dAtk = String.format("%d",
					CombatCalculator.calculateBaseDamage(defender, attacker));
			dCrit = String.format("%d", Math.max(0, Math.min(100, 
					defender.crit() - attacker.avoid())));
		}

		Renderer.drawRectangle(x, y, x + 90, y + 35, renderDepth,
				attacker.getPartyColor());
		Renderer.drawString("default_med", attacker.name, x + 2, y + 2,
				renderDepth);
		new ItemDisplay((int) x + 2, (int) y + 16, attacker.getWeapon())
				.render();

		Renderer.drawRectangle(x + 60, y + 35, x + 90, y + 96, renderDepth,
				attacker.getPartyColor());
		Renderer.drawString("default_med", aHp, x + 62, y + 36, renderDepth);
		Renderer.drawString("default_med", aAtk, x + 62, y + 51, renderDepth);
		Renderer.drawString("default_med", aHit, x + 62, y + 66, renderDepth);
		Renderer.drawString("default_med", aCrit, x + 62, y + 81, renderDepth);
		
		Renderer.drawRectangle(x+30, y+35, x+60, y+96, renderDepth, FightStage.NEUTRAL);
		Renderer.drawString("default_med", "HP", x+37, y+ 36, renderDepth);
		Renderer.drawString("default_med", "Atk", x+36, y+ 51, renderDepth);
		Renderer.drawString("default_med", "Hit", x+36, y+ 66, renderDepth);
		Renderer.drawString("default_med", "Crt", x+36, y+ 81, renderDepth);
		
		Renderer.drawRectangle(x, y + 35, x + 30, y + 96, renderDepth,
				defender.getPartyColor());
		Renderer.drawString("default_med", dHp, x + 2, y + 36, renderDepth);
		Renderer.drawString("default_med", dAtk, x + 2, y + 51, renderDepth);
		Renderer.drawString("default_med", dHit, x + 2, y + 66, renderDepth);
		Renderer.drawString("default_med", dCrit, x + 2, y + 81, renderDepth);
		
		Renderer.drawRectangle(x, y+96, x + 90, y + 131, renderDepth,
				defender.getPartyColor());
		Renderer.drawString("default_med", defender.name, x + 2, y + 98,
				renderDepth);
		new ItemDisplay((int) x + 2, (int) y + 112, defender.getWeapon())
				.render();
	}
}
