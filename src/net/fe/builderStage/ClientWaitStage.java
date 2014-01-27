package net.fe.builderStage;

import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.Session;
import net.fe.network.Message;
import net.fe.network.message.PartyMessage;
import net.fe.network.message.StartGame;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.unit.Unit;
import chu.engine.Game;
import chu.engine.anim.Renderer;

public class ClientWaitStage extends WaitStage {
	
	private boolean start;
	
	public ClientWaitStage(Session s) {
		super(s);
		start = false;
	}
	
	protected void init() {
		
	}
	
	public void beginStep() {
		for(Message message : Game.getMessages()) {
			if(message instanceof PartyMessage) {
				PartyMessage pm = (PartyMessage)message;
				for(Player p : session.getPlayers()){ 
					if(p.getID() == message.origin) {
						p.getParty().clear();
						for(Unit u : pm.teamData)
							p.getParty().addUnit(u);
					}
				}
			}
			if(message instanceof StartGame) {
				start = true;
			}
		}
	}
	
	public void endStep() {
		if(start) {
			for(Player p : session.getPlayers()) {
				for(Unit u : p.getParty()) {
					u.initializeEquipment();
				}
			}
			FEMultiplayer.map = new ClientOverworldStage(session);
			FEMultiplayer.setCurrentStage(FEMultiplayer.map);
		}
	}

	public void render() {
		Renderer.drawString("default_med", "Waiting for other players...", 200, 150, 0.0f);
	}

}
