package net.fe.builderStage;

import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.network.FEServer;
import net.fe.network.Message;
import net.fe.network.message.PartyMessage;
import net.fe.network.message.StartGame;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.unit.Unit;
import chu.engine.Game;
import chu.engine.anim.Renderer;

public class ClientWaitStage extends WaitStage {
	
	protected void init() {
		
	}
	
	public void beginStep() {
		boolean start = false;
		for(Message message : Game.getMessages()) {
			if(message instanceof PartyMessage) {
				PartyMessage pm = (PartyMessage)message;
				for(Player p : FEMultiplayer.players){ 
					if(p.getID() == message.origin) {
						p.getParty().clear();
						for(Unit u : pm.teamData)
							p.getParty().addUnit(u);
					}
				}
			}
			else if(message instanceof StartGame) {
				start = true;
			}
		}
		if(start) {
			FEMultiplayer.map = new ClientOverworldStage("test", FEMultiplayer.players);
			FEMultiplayer.setCurrentStage(FEMultiplayer.map);
		}
	}
	
	public void endStep() {
		
	}

	public void render() {
		Renderer.drawString("default_med", "Waiting for other players...", 100, 150, 0.0f);
	}

}
