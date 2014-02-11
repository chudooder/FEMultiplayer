package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.AnimationData;

public class MagicAttack extends AttackAnimation{

	public MagicAttack(AnimationData data, FightStage stage,
			AnimationArgs animArgs) {
		super(data, stage, animArgs);
	}

	@Override
	public void onLastHit() {
		stage.setDarkness(0.3f);
		stage.addEntity(new MagicEffect(animationArgs));
		stage.addEntity(new BackgroundEffect(getBgEffectName(animationArgs), 
				animationArgs.left));
		loopNextFrames(freeze);
		onHit();
		
	}

	@Override
	public void onHit() {
		//TODO: Magic sounds
	}
	
	
	public static String getBgEffectName(AnimationArgs args){
		return "bg_effect_" + args.unit.getWeapon().name.toLowerCase();
	}
}
