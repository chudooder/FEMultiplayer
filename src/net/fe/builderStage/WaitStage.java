package net.fe.builderStage;

import java.util.ArrayList;
import java.util.HashMap;

import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.network.FEServer;
import net.fe.network.Message;
import net.fe.network.message.PartyMessage;
import net.fe.network.message.StartGame;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;
import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

/**
 * Wait for all players to select
 * @author Shawn
 *
 */
public class WaitStage extends Stage {
	
	private HashMap<Byte, Boolean> readyStatus;
	private ArrayList<PartyMessage> messages;
	private boolean sentStartMessage;
	
	public WaitStage() {
		super("preparations");
		init();
	}
	
	protected void init() {
		readyStatus = new HashMap<Byte, Boolean>();
		sentStartMessage = false;
		for(Player p : FEServer.players) {
			if(!p.isSpectator()) readyStatus.put(p.getID(), false);
		}
		messages = new ArrayList<PartyMessage>();
	}

	@Override
	public void beginStep() {
		for(Message message : Game.getMessages()) {
			if(message instanceof PartyMessage) {
				PartyMessage pm = (PartyMessage)message;
				for(Player p : FEServer.players){ 
					if(p.getID() == message.origin) {
						p.getParty().clear();
						for(Unit u : pm.teamData)
							p.getParty().addUnit(u);
						readyStatus.put(p.getID(), true);
					}
				}
				messages.add(pm);
			}
		}
		
	}

	@Override
	public void onStep() {
		
	}

	@Override
	public void endStep() {
		if(!sentStartMessage) {
			for(boolean b : readyStatus.values()) {
				if(!b) return;
			}
			for(PartyMessage pm : messages) {
				FEServer.getServer().broadcastMessage(pm);
			}
			FEServer.getServer().broadcastMessage(new StartGame(0));
			for(Player p : FEServer.players) {
				for(Unit u : p.getParty()) {
					u.initializeEquipment();
				}
			}
			FEServer.setCurrentStage(new OverworldStage("town", FEServer.players));
			sentStartMessage = true;
		}
	}
	
}
