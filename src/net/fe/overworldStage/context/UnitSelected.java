package net.fe.overworldStage.context;

import net.fe.overworldStage.Zone;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

public class UnitSelected extends OverworldContext {
	private Zone move;
	private Zone attack;
	private Zone heal;
	public UnitSelected(OverworldStage s, OverworldContext prev, Unit u) {
		super(s, prev);
		this.move = new Zone(stage.grid.getPossibleMoves(u), Zone.MOVE_DARK);
		this.attack = Zone.minus(
				new Zone(stage.grid.getAttackRange(u),Zone.ATTACK_DARK), move);
		this.heal = Zone.minus(Zone.minus(
				new Zone(stage.grid.getHealRange(u),Zone.HEAL_DARK), move), attack);
		stage.addEntity(move);
		stage.addEntity(attack);
		stage.addEntity(heal);
	}

	@Override
	public void onSelect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancel() {
		stage.removeEntity(move);
		stage.removeEntity(attack);
		stage.removeEntity(heal);
		stage.setContext(prev);
	}

	@Override
	public void onUp() {
		stage.cursor.ycoord--;
	}

	@Override
	public void onDown() {
		stage.cursor.ycoord++;
	}

	@Override
	public void onLeft() {
		stage.cursor.xcoord--;
	}

	@Override
	public void onRight() {
		stage.cursor.xcoord++;
	}

}
