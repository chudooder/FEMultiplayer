package net.fe.overworldStage.context;

import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

public class TakeTarget extends SelectTargetContext {

	public TakeTarget(ClientOverworldStage stage, OverworldContext context, Zone z,
			Unit u) {
		super(stage, context, z, u, true);
	}
	
	public boolean validTarget(Unit u){
		return super.validTarget(u) && u.rescuedUnit() != null;
	}

	@Override
	public void unitSelected(Unit u) {
		stage.addCmd("TAKE");
		stage.addCmd(new UnitIdentifier(u));
		u.give(unit);
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		new UnitMoved(stage, this, unit, false, true).startContext();
	}

}
