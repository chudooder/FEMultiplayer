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
		loopNextFrames(freeze);
	}

}
