package net.fe.overworldStage.context;

import net.fe.FEMultiplayer;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

public class AttackTarget extends SelectTargetContext {
	public AttackTarget(OverworldStage stage, OverworldContext context,
			Zone z, Unit u, boolean friendly) {
		super(stage, context, z, u, friendly);
	}

	@Override
	public void updateCursor() {
		super.updateCursor();
		unit.equipFirstWeapon(Grid.getDistance(cursor.getXCoord(),
				cursor.getYCoord(), unit.getXCoord(), unit.getYCoord()));
	}

	@Override
	public void unitSelected(Unit u) {
		UnitIdentifier me = new UnitIdentifier(unit);
		UnitIdentifier you = new UnitIdentifier(unit);
		System.out.println("Selected " + u.name);
		unit.moved();
		stage.returnToNeutral();
		CombatCalculator calc = new CombatCalculator(
				new UnitIdentifier(unit), new UnitIdentifier(
						getHoveredUnit()));
		FEMultiplayer.setCurrentStage(new FightStage(
				new UnitIdentifier(unit), new UnitIdentifier(
						getHoveredUnit()), calc.getAttackQueue()));

	}

}
