package net.fe.overworldStage.context;

import net.fe.overworldStage.*;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

public class AttackTarget extends SelectTargetContext {
	public AttackTarget(OverworldStage stage, OverworldContext context, Zone z,
			Unit u, boolean friendly) {
		super(stage, context, z, u, friendly);
	}

	@Override
	public void updateCursor() {
		super.updateCursor();
		if (!friendly)
			unit.equipFirstWeapon(Grid.getDistance(cursor.getXCoord(),
					cursor.getYCoord(), unit.getXCoord(), unit.getYCoord()));
		else
			unit.equipFirstStaff(Grid.getDistance(cursor.getXCoord(),
					cursor.getYCoord(), unit.getXCoord(), unit.getYCoord()));
	}

	@Override
	public void unitSelected(Unit u) {
		UnitIdentifier you = new UnitIdentifier(u);
		if (friendly) {
			stage.addCmd("Heal");
			stage.addCmd(you);
			stage.send();

			unit.moved();
			cursor.setXCoord(unit.getXCoord());
			cursor.setYCoord(unit.getYCoord());
			stage.returnToNeutral();
		} else {
			new AttackWeapon(stage, this, unit, u).startContext();
		}
	}

}
