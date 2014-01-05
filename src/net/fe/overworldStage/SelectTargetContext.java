package net.fe.overworldStage;

import java.util.ArrayList;
import java.util.List;

import chu.engine.anim.AudioPlayer;
import net.fe.unit.Unit;

public abstract class SelectTargetContext extends OverworldContext {
	private Zone zone;
	private List<Unit> targets;
	protected int selected;
	protected Unit unit;
	protected boolean friendly;

	public SelectTargetContext(ClientOverworldStage stage, OverworldContext context, Zone z,
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
		super.cleanUp();
		stage.removeEntity(zone);

	}

	private void findTargets(Unit unit, boolean friendly) {
		targets.clear();
		for (Node n : zone.getNodes()) {
			Unit u = grid.getUnit(n.x, n.y);
			if(u!= null && validTarget(u)){
				targets.add(u);
			}
		}
	}
	
	public boolean validTarget(Unit u){
		return friendly == u.getParty().isAlly(stage.getCurrentPlayer().getParty());
	}

	@Override
	public void onSelect() {
		AudioPlayer.playAudio("select", 1, 1);
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
		AudioPlayer.playAudio("cursor", 1, 1);
		cursor.setXCoord(targets.get(selected).getXCoord());
		cursor.setYCoord(targets.get(selected).getYCoord());
	}

	@Override
	public void onCancel() {
		super.onCancel();
		//Reset the position of the cursor on cancels
		
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
	}

}
