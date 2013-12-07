package net.fe.overworldStage.context;

import net.fe.Player;
import net.fe.overworldStage.Zone;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

public class Idle extends OverworldContext {
	private Player player;
	private Zone move,attack,heal;
	
	public Idle(OverworldStage s, Player p) {
		super(s, null);
		player = p;
		Unit u = stage.getUnit(stage.cursor.xcoord, stage.cursor.ycoord);
		if(u!=null && !u.hasMoved()){
			addZones(u);
		}
	}

	@Override
	public void onSelect() {
		Unit u = stage.getUnit(stage.cursor.xcoord, stage.cursor.ycoord);
		if(u!=null && u.getParty() == player.getParty() && !u.hasMoved()){
			stage.setContext(new UnitSelected(stage, this, u));
		}

	}

	@Override
	public void onCancel() {
		//Nothing
	}

	@Override
	public void onUp() {
		stage.cursor.ycoord--;
		removeZones();
		Unit u = stage.getUnit(stage.cursor.xcoord, stage.cursor.ycoord);
		if(u!=null && !u.hasMoved()){
			addZones(u);
		}
	}

	@Override
	public void onDown() {
		stage.cursor.ycoord++;
		removeZones();
		Unit u = stage.getUnit(stage.cursor.xcoord, stage.cursor.ycoord);
		if(u!=null && !u.hasMoved()){
			addZones(u);
		}
	}

	@Override
	public void onLeft() {
		stage.cursor.xcoord--;
		removeZones();
		Unit u = stage.getUnit(stage.cursor.xcoord, stage.cursor.ycoord);
		if(u!=null && !u.hasMoved()){
			addZones(u);
		}
	}

	@Override
	public void onRight() {
		stage.cursor.xcoord++;
		removeZones();
		Unit u = stage.getUnit(stage.cursor.xcoord, stage.cursor.ycoord);
		if(u!=null && !u.hasMoved()){
			addZones(u);
		}
	}
	
	public void addZones(Unit u){
		this.move = new Zone(stage.grid.getPossibleMoves(u), Zone.MOVE_DARK);
		this.attack = Zone.minus(
				new Zone(stage.grid.getAttackRange(u),Zone.ATTACK_DARK), move);
		this.heal = Zone.minus(Zone.minus(
				new Zone(stage.grid.getHealRange(u),Zone.HEAL_DARK), move), attack);
		stage.addEntity(move);
		stage.addEntity(attack);
		stage.addEntity(heal);
	}
	
	public void removeZones(){
		stage.removeEntity(move);
		stage.removeEntity(attack);
		stage.removeEntity(heal);
	}

}
