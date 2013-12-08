package net.fe.overworldStage.context;

import java.util.*;

import net.fe.overworldStage.*;
import net.fe.unit.*;

public abstract class SelectTarget extends OverworldContext {
	private Zone zone;
	private List<Unit> targets;
	private int selected;
	private Unit unit;
	private boolean friendly;

	public SelectTarget(OverworldStage stage, OverworldContext context, Zone z,
			Unit u, boolean friendly) {
		super(stage, context);
		zone = z;
		targets = new ArrayList<Unit>();
		stage.addEntity(zone);
		this.unit = u;
		this.friendly = friendly;
		
	}
	
	public void startContext(){
		super.startContext();
		findTargets(unit, friendly);
		updateCursor();
	}

	private void findTargets(Unit unit, boolean friendly) {
		targets.clear();
		for (Node n : zone.getNodes()) {
			Unit u = grid.getUnit(n.x, n.y);
			if (u == null)
				continue;
			if (u.getParty().isAlly(stage.getPlayer().getParty()) == friendly
					&& u != unit) {
				targets.add(u);
			}
		}
	}

	@Override
	public void onSelect() {
		stage.removeEntity(zone);
		unitSelected(grid.getUnit(cursor.xcoord, cursor.ycoord));
	}

	public abstract void unitSelected(Unit u);

	public void onUp() {
		prevTarget();
	}

	public void onDown() {
		nextTarget();
	}

	public void onLeft() {
		prevTarget();
	}

	public void onRight() {
		nextTarget();
	}

	public void prevTarget() {
		selected--;
		if(selected < 0){
			selected+=targets.size();
		}
		updateCursor();
	}

	public void nextTarget() {
		selected++;
		selected%= targets.size();
		updateCursor();
	}

	public void updateCursor() {
		cursor.xcoord = targets.get(selected).xcoord;
		cursor.ycoord = targets.get(selected).ycoord;
	}

	@Override
	public void onCancel() {
		stage.removeEntity(zone);
		prev.startContext();
	}

}
