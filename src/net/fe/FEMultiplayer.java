package net.fe;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.util.ArrayList;

import net.fe.builderStage.TeamBuilderStage;
import net.fe.fightStage.AttackRecord;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.fightStage.HealCalculator;
import net.fe.lobbystage.ClientLobbyStage;
import net.fe.lobbystage.LobbyStage;
import net.fe.network.Client;
import net.fe.network.Message;
import net.fe.network.message.CommandMessage;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Terrain;
import net.fe.transition.OverworldFightTransition;
import net.fe.unit.HealingItem;
import net.fe.unit.Unit;
import net.fe.unit.UnitFactory;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.WeaponFactory;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.newdawn.slick.openal.SoundStore;

import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class FEMultiplayer extends Game{
	private static Stage currentStage;
	private static Client client;
	public static ArrayList<Player> players;
	private static Player localPlayer;
	
	public static Player turn;
	public static ClientOverworldStage map;
	public static ClientLobbyStage lobby;
	
	public static void main(String[] args) {
		FEMultiplayer game = new FEMultiplayer();
		game.init(480, 320, "Fire Emblem Multiplayer");
		game.loop();
		
	}
	
	public void init(int width, int height, String name) {
		super.init(width, height, name);
		players = new ArrayList<Player>();
		Player p1 = new Player("Chu", (byte) 0);
		localPlayer = p1;
		players.add(p1);
		FEResources.loadResources();
		p1.getParty().setColor(Party.TEAM_BLUE);
		
		/* OpenGL final setup */
		glEnable(GL_LINE_SMOOTH);

		Unit u1 = UnitFactory.getUnit("Lyn");
		u1.addToInventory(WeaponFactory.getWeapon("Brave Sword"));
		u1.addToInventory(WeaponFactory.getWeapon("Debug Bow"));
		u1.addToInventory(HealingItem.CONCOCTION.getCopy());
		u1.use(3);
		u1.use(3);
		u1.equipFirstWeapon(1);
		p1.getParty().addUnit(u1);
		
		Unit u3 = UnitFactory.getUnit("Priscilla");
		u3.addToInventory(WeaponFactory.getWeapon("Elfire"));
		u3.addToInventory(WeaponFactory.getWeapon("Physic"));
		u3.addToInventory(WeaponFactory.getWeapon("Recover"));
		u3.addToInventory(HealingItem.VULNERARY.getCopy());
		u3.equipFirstWeapon(1);
		p1.getParty().addUnit(u3);

		Unit u2 = UnitFactory.getUnit("Eliwood");
		u2.addToInventory(WeaponFactory.getWeapon("Brave Lance"));
		u2.addToInventory(WeaponFactory.getWeapon("Debug Lance"));
		u2.addToInventory(WeaponFactory.getWeapon("Debug Javelin"));
		u2.equipFirstWeapon(1);
		p1.getParty().addUnit(u2);

		u1.setLevel(20);
		u2.setLevel(20);
		u3.setLevel(20);
		
		u1.fillHp();
		u2.fillHp();
		u3.fillHp();
		u3.setHp(1);
		
//		map = new OverworldStage("test", p1);
		
		client = new Client();
		client.start();
		lobby = new ClientLobbyStage();
		currentStage = lobby;
		messages = new ArrayList<Message>();
		
	}
	
	public static Unit getUnit(UnitIdentifier id){
		for(Player p: players){
			if(!p.isSpectator() && p.getParty().getColor().equals(id.partyColor)){
				return p.getParty().search(id.name);
			}
		}
		return null;
	}

	@Override
	public void loop() {
		while(!Display.isCloseRequested()) {
			time = System.nanoTime();
			glClear(GL_COLOR_BUFFER_BIT |
			        GL_DEPTH_BUFFER_BIT |
			        GL_STENCIL_BUFFER_BIT);
			glClearDepth(1.0f);
			getInput();
			messages.clear();
			messages.addAll(client.getMessages());
			for(Message m : messages)
				client.messages.remove(m);
			SoundStore.get().poll(0);
			glPushMatrix();
			if(!paused) {
				currentStage.beginStep();
				currentStage.onStep();
				currentStage.processAddStack();
				currentStage.processRemoveStack();
				Renderer.getCamera().lookThrough();
				currentStage.render();
				FEResources.getBitmapFont("stat_numbers").render(
						(int)(1.0f/getDeltaSeconds())+"", 440f, 0f, 0f);
				currentStage.endStep();
			}
			glPopMatrix();
			Display.update();
			timeDelta = System.nanoTime()-time;
		}
		AL.destroy();
		Display.destroy();
		if(client.isOpen()) client.quit();
	}
	
	public static void reportFightResults(FightStage stage){ 
		//TODO report weapon usage, stun trigger
		for(int i=0; i<2; i++) {
			if(stage.getUnit(i).getHp() <= 0) {
				stage.getUnit(i).setDying(true);
			}
		}
	}
	
	public static void send(UnitIdentifier u, int moveX, int moveY, Object... cmds){
		//TODO server stuff
		System.out.print("SEND> " + u.name + " moved (" + moveX + "," + moveY + ") - ");
		for(Object o: cmds){
			System.out.print(o + " ");
		}
		System.out.println();
		client.sendMessage(new CommandMessage(u, moveX, moveY, null, cmds));
	}
	
	public static void goToFightStage(UnitIdentifier u, UnitIdentifier other, 
			ArrayList<AttackRecord> queue) {
			FightStage to = new FightStage(u, other, queue);
			currentStage.addEntity(new OverworldFightTransition(to, u, other));
	}
	
	public static Player getLocalPlayer() {
		return localPlayer;
	}

	
	public static void setCurrentStage(Stage stage) {
		currentStage = stage;
	}

	public static Stage getOverworldStage() {
		return map;
	}
	
	public static Client getClient() {
		return client;
	}

	public static Stage getCurrentStage() {
		return currentStage;
	}

	public static void setLocalPlayer(Player p) {
		localPlayer = p;
	}

}
