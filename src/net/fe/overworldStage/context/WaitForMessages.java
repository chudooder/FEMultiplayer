package net.fe.overworldStage.context;

import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.CursorContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;
import chu.engine.anim.AudioPlayer;

public class WaitForMessages extends CursorContext {
	private Zone move,attack,heal;
	
	public WaitForMessages(ClientOverworldStage s) {
		super(s, null);
	}
	
	public void startContext(){
		super.startContext();
		cursorChanged();
	}
	
	
	public void cleanUp(){
		removeZones();
	}

	@Override
	public void onSelect() {
		//Nothing
	}


	@Override
	public void onCancel() {
		//Nothing
	}
	
	public void cursorWillChange(){
		removeZones();
		Unit u = getHoveredUnit();
		if(u!=null && !u.hasMoved()){
			u.sprite.setAnimation("IDLE");
		}
	}

	public void cursorChanged(){
		Unit u = getHoveredUnit();
		AudioPlayer.playAudio("cursor", 1, 1);
		if(u!=null && !u.hasMoved()){
			addZones(u);
			if(u.getParty() == stage.getCurrentPlayer().getParty()){
				u.sprite.setAnimation("SELECTED");
			}
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
