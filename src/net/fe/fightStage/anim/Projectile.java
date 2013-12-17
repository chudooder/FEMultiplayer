package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import net.fe.unit.Weapon;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Resources;
import chu.engine.anim.Transform;

public class Projectile extends Entity {
	private float destination;
	private int dir;
	private boolean destroyOnHit;
	private boolean hit;
	private String name;
	
	public Projectile(String name, float y, FightStage f, 
			boolean left, boolean destroyOnHit, AnimationArgs args){
		super(0,y);
		// TODO getTextures
		if(args.wepAnimName.equals("javelin"))
			sprite.addAnimation("default", Resources.getTexture("proj_javelin"));
		else
			sprite.addAnimation("default", Resources.getTexture("proj_arrow"));
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
		this.name = name;
		renderDepth = FightStage.EFFECT_DEPTH;
		hit = false;
	}
	
	public void onStep(){
		x += dir*getVelocity(name)*40*Game.getDeltaSeconds();
	}
	
	public void render(){
		Transform t = new Transform();
		if(dir == 1) t.flipHorizontal();
		sprite.renderTransformed(x, y, renderDepth, t);
	}
	
	public void endStep(){
		if(dir*x > dir*destination && !hit){
			getStage().setCurrentEvent(FightStage.ATTACKED);
			hit = true;
			if(destroyOnHit){
				destroy();
			}
		}
		//TODO Destroy if out of bounds
		if(x > 500 || x < -500)
			destroy();
	}

	public FightStage getStage(){
		return (FightStage) stage;
	}
	
	public static int getVelocity(String name){
		//TODO
		return 30;
	}
}
