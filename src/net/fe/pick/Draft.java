package net.fe.pick;

import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.Session;
import net.fe.builderStage.ClientWaitStage;
import net.fe.builderStage.TeamDraftStage;
import net.fe.builderStage.WaitStage;
import net.fe.network.FEServer;

public class Draft implements PickMode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6216203906090478045L;

	@Override
	public void setUpClient(Session session) {
		for(Player p : session.getPlayers()) {
			p.getParty().clear();
		}
		if(!FEMultiplayer.getLocalPlayer().isSpectator()) {
			TeamDraftStage stage = new TeamDraftStage(session);
			FEMultiplayer.setCurrentStage(stage);
		} else {
			ClientWaitStage stage = new ClientWaitStage(session);
			FEMultiplayer.setCurrentStage(stage);
		}
	}

	@Override
	public void setUpServer(Session session) {
		for(Player p : session.getPlayers()) {
			p.getParty().clear();
		}
		FEServer.setCurrentStage(new WaitStage(session));
	}
	
	public String toString() {
		return "Draft";
	}

}
