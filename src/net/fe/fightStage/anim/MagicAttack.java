package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.TextureData;

public class MagicAttack extends AttackAnimation{

	public MagicAttack(TextureData data, FightStage stage,
			AnimationArgs animArgs) {
		super(data, stage, animArgs);
	}

	@Override
	public void onHit() {
		stage.addEntity(new MagicEffect(animationArgs));
		stage.addEntity(new BackgroundEffect(animationArgs.unit.getWeapon().name.toLowerCase(), 
				animationArgs.left));
		loopNextFrames(freeze);
	}

}
