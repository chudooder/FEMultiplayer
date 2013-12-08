package net.fe.overworldStage.context;

import net.fe.Command;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;

public class UnitSelected extends CursorContext {
	private Zone move, attack, heal;
	private Unit selected;
	private Path path;
	private int oX, oY;

	public UnitSelected(OverworldStage s, OverworldContext prev, Unit u) {
		super(s, prev);
		selected = u;
		oX = u.xcoord;
		oY = u.ycoord;
	}
	
	public void startContext(){
		super.startContext();
		this.move = new Zone(grid.getPossibleMoves(selected), Zone.MOVE_DARK);
		this.attack = Zone.minus(new Zone(grid.getAttackRange(selected),
				Zone.ATTACK_DARK), move);
		this.heal = Zone.minus(Zone.minus(new Zone(grid.getHealRange(selected),
				Zone.HEAL_DARK), move), attack);
		stage.addEntity(move);
		stage.addEntity(attack);
		stage.addEntity(heal);
		updatePath();
	}

	@Override
	public void onSelect() {
		if (move.getNodes().contains(new Node(cursor.xcoord, cursor.ycoord))) {
			grid.move(selected.xcoord, selected.ycoord, cursor.xcoord,
					cursor.ycoord);
			selected.move(path, new Command() {
				@Override
				public void execute() {
					stage.removeEntity(attack);
					stage.removeEntity(move);
					stage.removeEntity(heal);
					stage.removeEntity(path);
					new UnitMoved(stage, UnitSelected.this,
							selected, oX, oY).startContext();
				}
			});
		}
	}

	@Override
	public void onCancel() {
		stage.removeEntity(move);
		stage.removeEntity(attack);
		stage.removeEntity(heal);
		stage.removeEntity(path);
		cursor.xcoord = oX;
		cursor.ycoord = oY;
		prev.startContext();
	}

	public void cursorChanged() {
		updatePath();
	}

	private void updatePath() {
		stage.removeEntity(path);
		path = stage.grid.getShortestPath(selected, cursor.xcoord,
				cursor.ycoord);
		if (path != null) {
			stage.addEntity(path);
		}
	}

}
