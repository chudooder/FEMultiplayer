package net.fe.pick;

import net.fe.FEMultiplayer;
import net.fe.Session;
import net.fe.builderStage.ClientWaitStage;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.WaitStage;
import net.fe.network.FEServer;

public class AllPick implements PickMode {

	@Override
	public void setUpClient(Session session) {
		if(!FEMultiplayer.getLocalPlayer().isSpectator()) {
			TeamBuilderStage stage = new TeamBuilderStage(false, session);
			FEMultiplayer.setCurrentStage(stage);
		} else {
			ClientWaitStage stage = new ClientWaitStage(session);
			FEMultiplayer.setCurrentStage(stage);
		}
	}

	@Override
	public void setUpServer(Session session) {
		FEServer.setCurrentStage(new WaitStage(session));
	}

}
