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
	public static ConnectStage connect;
	
	public static void main(String[] args) {
		
		FEMultiplayer game = new FEMultiplayer();
		game.init(480, 320, "Fire Emblem Multiplayer");
		game.loop();

	}
	
	
	
	public void init(int width, int height, String name) {
		super.init(width, height, name);
		players = new ArrayList<Player>();
		Player p1 = new Player("Player", (byte) 0);
		localPlayer = p1;
		players.add(p1);
		FEResources.loadResources();
		p1.getParty().setColor(Party.TEAM_BLUE);
		
		/* OpenGL final setup */
		glEnable(GL_LINE_SMOOTH);
		
		UnitFactory.getUnit("Lyn");
		connect = new ConnectStage();
		currentStage = new TitleStage();
		testFightStage();
		messages = new ArrayList<Message>();
		
	}
	
	public void testFightStage(){
		Player p1 = localPlayer;
		Player p2 = new Player("p2", (byte) 1);
		p2.getParty().setColor(Party.TEAM_RED);
		players.add(p2);
		
		Grid grid = new Grid(10,10, Terrain.PLAIN);
		Unit u1 = UnitFactory.getUnit("Lute");
		u1.addToInventory(WeaponFactory.getWeapon("Lightning"));
		grid.addUnit(u1, 0, 0);
		u1.equip(0);
		p1.getParty().addUnit(u1);
		
		Unit u2 = UnitFactory.getUnit("Marth");
		grid.addUnit(u2, 1, 0);
		u2.equip(0);
		p2.getParty().addUnit(u2);
		
		CombatCalculator calc = new CombatCalculator(new UnitIdentifier(u1), new UnitIdentifier(u2), true);
		System.out.println(calc.getAttackQueue());
		u1.fillHp();
		u2.fillHp();
		currentStage = new FightStage(new UnitIdentifier(u1), new UnitIdentifier(u2), calc.getAttackQueue());
	}
	
	public static Unit getUnit(UnitIdentifier id){
		for(Player p: players){
			if(!p.isSpectator() && p.getParty().getColor().equals(id.partyColor)){
				return p.getParty().search(id.name);
			}
		}
		return null;
	}
	
	public static void connect(String nickname, String ip) {
		getLocalPlayer().setName(nickname);
		lobby = new ClientLobbyStage();
		setCurrentStage(lobby);
		client = new Client(ip, 21255);
		client.start();
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
			if(client != null){
				messages.addAll(client.getMessages());
				for(Message m : messages)
					client.messages.remove(m);
			}
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
		if(client != null && client.isOpen()) client.quit();
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
