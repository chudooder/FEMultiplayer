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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import net.fe.fightStage.AttackRecord;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.lobbystage.ClientLobbyStage;
import net.fe.modifier.Divine;
import net.fe.network.Client;
import net.fe.network.Message;
import net.fe.network.message.CommandMessage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.Terrain;
import net.fe.overworldStage.objective.Seize;
import net.fe.transition.OverworldFightTransition;
import net.fe.unit.Unit;
import net.fe.unit.UnitFactory;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.WeaponFactory;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.openal.SoundStore;

import chu.engine.Game;
import chu.engine.Stage;

public class FEMultiplayer extends Game{
	private static Stage currentStage;
	private static Client client;
	private static Player localPlayer;
	
	public static Player turn;
	public static ClientOverworldStage map;
	public static ClientLobbyStage lobby;
	public static ConnectStage connect;
	
	// For testing fightstage
	private static Session testSession;
	
	public static void main(String[] args) {
		FEMultiplayer game = new FEMultiplayer();
		game.init(480, 320, "Fire Emblem Multiplayer");
//		game.testFightStage();
		game.testOverworldStage();
		game.loop();
	}
	
	
	
	public void init(int width, int height, String name) {
		super.init(width, height, name);
		Player p1 = new Player("Player", (byte) 0);
		localPlayer = p1;
		ByteBuffer icon16 = ByteBuffer.wrap(FEResources.getTexture("icon16").getTextureData());
		Display.setIcon(new ByteBuffer[]{icon16});
		FEResources.loadResources();
		FEResources.loadBitmapFonts();
		p1.getParty().setColor(Party.TEAM_BLUE);
		
		/* OpenGL final setup */
		glEnable(GL_LINE_SMOOTH);
		
		UnitFactory.getUnit("Lyn");
		connect = new ConnectStage();
		setCurrentStage(new TitleStage());
		messages = new ArrayList<Message>();
		SoundTrack.loop("main_theme");
		
	}
	
	public void testFightStage(){
		Player p1 = localPlayer;
		testSession = new Session();
		Player p2 = new Player("p2", (byte) 1);
		p2.getParty().setColor(Party.TEAM_RED);
		testSession.addPlayer(p1);
		testSession.addPlayer(p2);
		
		Grid grid = new Grid(10,10, Terrain.PLAIN);
		
		Unit u1 = UnitFactory.getUnit("Ike");
		u1.equip(0);
		u1.setHp(2);
		u1.addSkill(new Divine());
		grid.addUnit(u1, 0, 0);
		p1.getParty().addUnit(u1);
		
		Unit u2 = UnitFactory.getUnit("Ephraim");
		System.out.println(u2);
		grid.addUnit(u2, 1, 0);
		u2.equip(0);
		u2.setLevel(20);
		p2.getParty().addUnit(u2);
		
		CombatCalculator calc = new CombatCalculator(new UnitIdentifier(u1), new UnitIdentifier(u2), true);
		System.out.println(calc.getAttackQueue());
		u1.setHp(2);
		u2.fillHp();
		setCurrentStage(new FightStage(new UnitIdentifier(u1), new UnitIdentifier(u2), calc.getAttackQueue()));
	}
	
	public void testOverworldStage() {
		testSession = new Session();
		testSession.setMap("town");
		testSession.setObjective(new Seize());
		testSession.addPlayer(localPlayer);
		
		Player p2 = new Player("P2", (byte)1);
		p2.getParty().setColor(Party.TEAM_RED);
		testSession.addPlayer(p2);
		
		Unit u1 = UnitFactory.getUnit("Franz");
		u1.addToInventory(WeaponFactory.getWeapon("Iron Lance"));
		u1.addToInventory(WeaponFactory.getWeapon("Divine"));
		u1.addToInventory(WeaponFactory.getWeapon("Divine"));
		u1.equip(0);
		u1.setHp(1);
		localPlayer.getParty().addUnit(u1);
		
		Unit u3 = UnitFactory.getUnit("Marth");
		u3.setHp(1);
		localPlayer.getParty().addUnit(u3);
		
		Unit u2 = UnitFactory.getUnit("Lute");
		u2.addToInventory(WeaponFactory.getWeapon("Physic"));
		localPlayer.getParty().addUnit(u2);
		
		currentStage = new ClientOverworldStage(testSession);

	}
	
	public static Unit getUnit(UnitIdentifier id){
		for(Player p : getPlayers().values()){
			if(!p.isSpectator() && p.getParty().getColor().equals(id.partyColor)){
				return p.getParty().search(id.name);
			}
		}
		return null;
	}
	
	public static void connect(String nickname, String ip) {
		getLocalPlayer().setName(nickname);
		client = new Client(ip, 21255);
		lobby = new ClientLobbyStage(client.getSession());
		setCurrentStage(lobby);
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
			currentStage.addEntity(new OverworldFightTransition((ClientOverworldStage)currentStage, to, u, other));
	}
	
	public static void setCurrentStage(Stage stage) {
		currentStage = stage;
		if(stage.soundTrack != null){
			SoundTrack.loop(stage.soundTrack);
		}
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
	
	public static Player getLocalPlayer() {
		return localPlayer;
	}
	
	public static HashMap<Byte, Player> getPlayers() {
		return getSession().getPlayerMap();
	}
	
	public static Session getSession() {
		if(client == null)
			return testSession;
		return client.getSession();
	}

}
