package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.AnimationData;
import chu.engine.anim.AudioPlayer;

public class ProjectileAttack extends AttackAnimation{
	public ProjectileAttack(AnimationData data, FightStage stage,
			AnimationArgs animArgs) {
		super(data, stage, animArgs);
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
		stage.setCurrentEvent(FightStage.ATTACKED);
	}

	@Override
	public void onHit() {
		
	}

}
