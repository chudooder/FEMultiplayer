package net.fe.fightStage;

import net.fe.unit.Unit;
import chu.engine.TextureData;

public class NormalAttack extends AttackAnimation {
	public NormalAttack(TextureData data, FightStage stage, Unit u) {
		super(data, stage, u);
	}

	@Override
	public void onHit() {
		stage.setCurrentEvent(FightStage.ATTACKED);
		loopNextFrames(freeze);
	}

}
