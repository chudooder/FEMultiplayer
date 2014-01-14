package net.fe.overworldStage.context;

import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;

public class TradeTarget extends SelectTargetContext {

	public TradeTarget(ClientOverworldStage stage, OverworldContext context, Zone z,
			Unit u) {
		super(stage, context, z, u, true);
	}

	@Override
	public void unitSelected(Unit u) {
		new TradeContext(stage, this, unit, u).startContext();
	}

}
