package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.List;

import chu.engine.anim.AudioPlayer;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;

public class DropTarget extends OverworldContext {
	private Zone zone;
	private List<Node> targets;
	protected int selected;
	protected Unit unit;

	public DropTarget(ClientOverworldStage stage, OverworldContext context, Zone z,
			Unit u) {
		super(stage, context);
		zone = z;
		targets = new ArrayList<Node>();
		this.unit = u;
		
	}
	
	public void startContext(){
		super.startContext();
		findTargets(unit);
		stage.addEntity(zone);
		updateCursor();
	}
	
	public void cleanUp(){
		super.cleanUp();
		stage.removeEntity(zone);

	}

	private void findTargets(Unit unit) {
		targets.clear();
		for (Node n : zone.getNodes()) {
			Unit u = grid.getUnit(n.x, n.y);
			if(u == null){
				targets.add(n);
			}
		}
	}

	@Override
	public void onSelect() {
		AudioPlayer.playAudio("select", 1, 1);
		stage.addCmd("DROP", getCurrentTarget().x, getCurrentTarget().y);
		stage.send();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}

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
	
	public Node getCurrentTarget(){
		return targets.get(selected);
	}

	public void updateCursor() {
		AudioPlayer.playAudio("cursor", 1, 1);
		cursor.setXCoord(targets.get(selected).x);
		cursor.setYCoord(targets.get(selected).y);
	}

	@Override
	public void onCancel() {
		super.onCancel();
		//Reset the position of the cursor on cancels
		
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
	}

}
