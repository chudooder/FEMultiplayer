package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.TextureData;

public class ProjectileAttack extends AttackAnimation{
	private boolean weFailed;
	public ProjectileAttack(TextureData data, FightStage stage,
			AnimationArgs animArgs) {
		super(data, stage, animArgs);
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
	public void onHit() {
		stage.addEntity(new Projectile(
				animationArgs.wepAnimName, 
				FightStage.FLOOR - 25,
				stage, animationArgs.left,
				animationArgs.wepAnimName.equals("javelin")));
	}

}
