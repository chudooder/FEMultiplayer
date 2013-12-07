package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.TextureData;

public class NormalAttack extends AttackAnimation {
	public NormalAttack(TextureData data, FightStage stage, AnimationArgs args) {
		super(data, stage, args);
	}

	@Override
	public void onHit() {
		stage.setCurrentEvent(FightStage.ATTACKED);
		loopNextFrames(freeze);
	}

}
