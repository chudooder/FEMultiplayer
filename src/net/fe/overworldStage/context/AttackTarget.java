package net.fe.overworldStage.context;

import net.fe.overworldStage.*;
import net.fe.unit.Unit;

public class AttackTarget extends SelectTargetContext {
	public AttackTarget(ClientOverworldStage stage, OverworldContext context, Zone z,
			Unit u) {
		super(stage, context, z, u, false);
	}

	@Override
	public void updateCursor() {
		super.updateCursor();
		unit.equipFirstWeapon(Grid.getDistance(unit, getCurrentTarget()));
	}

	@Override
	public void unitSelected(Unit u) {
		new AttackPreview(stage, this, unit, u).startContext();
	}

}
