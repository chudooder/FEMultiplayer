package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.TextureData;
import chu.engine.anim.AudioPlayer;

public class NormalAttack extends AttackAnimation {
	public NormalAttack(TextureData data, FightStage stage, AnimationArgs args) {
		super(data, stage, args);
	}

	@Override
	public void onLastHit() {
		stage.setCurrentEvent(FightStage.ATTACKED);
		if(sprite.getAnimationName().contains("CRIT"))
			AudioPlayer.playAudio("crit", 1, 1);
		else
			onHit();
		loopNextFrames(freeze);
	}

	@Override
	public void onHit() {
		AudioPlayer.playAudio("hit"+(int)(Math.random()*3), 1, 1);
	}

}
