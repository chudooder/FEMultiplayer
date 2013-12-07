package net.fe.overworldStage.context;

import net.fe.Command;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;

public class UnitSelected extends CursorContext {
	private Zone move;
	private Zone attack;
	private Zone heal;
	private Unit selected;
	private Path path;
	public UnitSelected(OverworldStage s, OverworldContext prev, Unit u) {
		super(s, prev);
		this.move = new Zone(grid.getPossibleMoves(u), Zone.MOVE_DARK);
		this.attack = Zone.minus(
				new Zone(grid.getAttackRange(u),Zone.ATTACK_DARK), move);
		this.heal = Zone.minus(Zone.minus(
				new Zone(grid.getHealRange(u),Zone.HEAL_DARK), move), attack);
		stage.addEntity(move);
		stage.addEntity(attack);
		stage.addEntity(heal);
		selected = u;
	}

	@Override
	public void onSelect() {
		if(move.getNodes().contains(new Node(cursor.xcoord, cursor.ycoord))){
			grid.move(selected.xcoord, selected.ycoord, cursor.xcoord, cursor.ycoord);
			selected.move(path, new Command(){
				@Override
				public void execute() {
					stage.addEntity(new Menu(0,0,"Attack", "Heal", "Wait"));
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
		stage.setContext(prev);
	}

	public void cursorChanged(){
		updatePath();
	}
	
	
	private void updatePath(){
		stage.removeEntity(path);
		path = stage.grid.getShortestPath(
				selected, cursor.xcoord, cursor.ycoord);
		if(path!=null){
			stage.addEntity(path);
		}
	}

}
