package net.fe.pick;

import net.fe.FEMultiplayer;
import net.fe.Session;
import net.fe.builderStage.ClientWaitStage;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.WaitStage;
import net.fe.network.FEServer;

public class AllPick implements PickMode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6038985000232148129L;

	@Override
	public void setUpClient(Session session) {
		if(!FEMultiplayer.getLocalPlayer().isSpectator()) {
			TeamBuilderStage stage = new TeamBuilderStage(false, null, session);
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
	
	public String toString() {
		return "All Pick";
	}

}
