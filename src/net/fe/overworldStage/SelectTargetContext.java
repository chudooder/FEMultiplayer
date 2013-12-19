package net.fe.overworldStage;

import java.util.ArrayList;
import java.util.List;

import net.fe.unit.Unit;

public abstract class SelectTargetContext extends OverworldContext {
	private Zone zone;
	private List<Unit> targets;
	protected int selected;
	protected Unit unit;
	protected boolean friendly;

	public SelectTargetContext(OverworldStage stage, OverworldContext context, Zone z,
			Unit u, boolean friendly) {
		super(stage, context);
		zone = z;
		targets = new ArrayList<Unit>();
		this.unit = u;
		this.friendly = friendly;
		
	}
	
	public void startContext(){
		super.startContext();
		findTargets(unit, friendly);
		stage.addEntity(zone);
		updateCursor();
	}
	
	public void cleanUp(){
		stage.removeEntity(zone);
		//Not sure why this is here.
//		cursor.setXCoord(unit.getXCoord());
//		cursor.setYCoord(unit.getYCoord());
	}

	private void findTargets(Unit unit, boolean friendly) {
		targets.clear();
		for (Node n : zone.getNodes()) {
			Unit u = grid.getUnit(n.x, n.y);
			if (u == null)
				continue;
			if(friendly) {
				if (u.getParty().isAlly(stage.getPlayer().getParty()) 
						&& u != unit && u.getHp() != u.get("HP")) {
					targets.add(u);
				}
			} else {
				if (!u.getParty().isAlly(stage.getPlayer().getParty()) 
						&& u != unit) {
					targets.add(u);
				}
			}
		}
	}

	@Override
	public void onSelect() {
		unitSelected(grid.getUnit(cursor.getXCoord(), cursor.getYCoord()));
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
	
	public Unit getCurrentTarget(){
		return targets.get(selected);
	}

	public void updateCursor() {
		cursor.setXCoord(targets.get(selected).getXCoord());
		cursor.setYCoord(targets.get(selected).getYCoord());
	}

	@Override
	public void onCancel() {
		prev.startContext();
	}

}
