package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.AnimationData;
import chu.engine.anim.AudioPlayer;

public class NormalAttack extends AttackAnimation {
	public NormalAttack(AnimationData data, FightStage stage, AnimationArgs args) {
		super(data, stage, args);
	}

	@Override
	public void onLastHit() {
		stage.setCurrentEvent(FightStage.HIT_EFFECT);
		loopNextFrames(freeze);
	}

	@Override
	public void onHit() {
		AudioPlayer.playAudio("hit_normal", 1, 1);
	}

}
