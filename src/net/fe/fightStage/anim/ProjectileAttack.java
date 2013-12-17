package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.AnimationData;
import chu.engine.anim.AudioPlayer;

public class ProjectileAttack extends AttackAnimation{
	private boolean weFailed;
	private AnimationArgs animArgs;
	public ProjectileAttack(AnimationData data, FightStage stage,
			AnimationArgs animArgs) {
		super(data, stage, animArgs);
		this.animArgs = animArgs;
	}
	
	public void done(){
		try{
			super.done();
		} catch (IllegalArgumentException e){
			//Drats! We'll try again next frame.
			weFailed = true;
			setFrame(getLength()-1);
		}
		
	}
	
	public void onStep(){
		if(weFailed){
			try{
				stage.setCurrentEvent(FightStage.DONE);
				weFailed = false;
			} catch (IllegalArgumentException e){
				//Foiled again! We'll try again next frame.
				weFailed = true;
			}
		}
	}

	@Override
	public void onLastHit() {
		stage.addEntity(new Projectile(
				animationArgs.wepAnimName, 
				FightStage.FLOOR - 25,
				stage, animationArgs.left,
				animationArgs.wepAnimName.equals("javelin"),
				animArgs));
		onHit();
	}

	@Override
	public void onHit() {
		AudioPlayer.playAudio("hit", 1, 1);
	}

}
