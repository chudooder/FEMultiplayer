package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.AnimationData;

public class ProjectileAttack extends AttackAnimation{
	private boolean ididit;
	public ProjectileAttack(AnimationData data, FightStage stage,
			AnimationArgs animArgs) {
		super(data, stage, animArgs);
	}
	
	public void update(){
		super.update();
		if(getFrame() >= hitframes[0] && !ididit){
			ididit = true;
			((FightStage) stage).moveCamera(animationArgs.left);
		}
	}
	
	public void done(){
		try{
			super.done();
		} catch (IllegalArgumentException e){
			//Drats! We'll try again next frame.
			setFrame(getLength()-1);
		}
		
	}

	@Override
	public void onLastHit() {
		stage.setCurrentEvent(FightStage.HIT_EFFECT);
	}

	@Override
	public void onHit() {
		
	}

}
