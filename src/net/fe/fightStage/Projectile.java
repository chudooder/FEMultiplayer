package net.fe.fightStage;

import chu.engine.Entity;
import chu.engine.Game;

public class Projectile extends Entity {
	private float destination;
	private int dir;
	private boolean destroyOnHit;
	
	public Projectile(String name, float y, FightStage f, 
			boolean left, boolean destroyOnHit){
		super(0,y);
		// TODO getTextures
		if(left){
			x = FightStage.CENTRAL_AXIS - f.distanceToHead();
			destination = FightStage.CENTRAL_AXIS + f.distanceToHead();
			dir = 1;
		} else {
			destination = FightStage.CENTRAL_AXIS - f.distanceToHead();
			x = FightStage.CENTRAL_AXIS + f.distanceToHead();
			dir = -1;
		}
		this.destroyOnHit = destroyOnHit;
		
	}
	
	public void onStep(){
		x += dir*30*Game.getDeltaSeconds();
	}
	
	public void endStep(){
		if(dir*x > dir*destination){
			getStage().setCurrentEvent(FightStage.ATTACKED);
			if(destroyOnHit){
				destroy();
			}
		}
		//TODO Destroy if out of bounds
	}

	public FightStage getStage(){
		return (FightStage) stage;
	}
}
