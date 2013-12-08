package net.fe.overworldStage.context;

import net.fe.Player;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;

public class Idle extends CursorContext {
	private Player player;
	private Zone move,attack,heal;
	
	public Idle(OverworldStage s, Player p) {
		super(s, null);
		player = p;
	}
	
	public void startContext(){
		super.startContext();
		Unit u = getHoveredUnit();
		if(u!=null && !u.hasMoved()){
			addZones(u);
		}
	}

	@Override
	public void onSelect() {
		Unit u = getHoveredUnit();
		if(u!=null && u.getParty() == player.getParty() && !u.hasMoved()){
			removeZones();
			new UnitSelected(stage, this, u).startContext();;
		}

	}

	@Override
	public void onCancel() {
		//Nothing
	}

	public void cursorChanged(){
		removeZones();
		Unit u = getHoveredUnit();
		if(u!=null && !u.hasMoved()){
			addZones(u);
		}
	}
	
	public void addZones(Unit u){
		this.move = new Zone(stage.grid.getPossibleMoves(u), Zone.MOVE_LIGHT);
		this.attack = Zone.minus(
				new Zone(stage.grid.getAttackRange(u),Zone.ATTACK_LIGHT), move);
		this.heal = Zone.minus(Zone.minus(
				new Zone(stage.grid.getHealRange(u),Zone.HEAL_LIGHT), move), attack);
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
