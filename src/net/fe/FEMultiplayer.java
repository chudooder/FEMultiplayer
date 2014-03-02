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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import net.fe.builderStage.TeamDraftStage;
import net.fe.fightStage.AttackRecord;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.lobbystage.ClientLobbyStage;
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
import org.newdawn.slick.Color;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.menu.Notification;

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
		try{
			FEMultiplayer game = new FEMultiplayer();
//			SoundTrack.enabled = false;
			game.init(480, 320, "Fire Emblem Multiplayer");
			/* Testing code */
//			game.testFightStage();
//			game.testOverworldStage();
//			game.testDraftStage();
			game.loop();
		} catch (Exception e){
			System.err.println("Exception occurred, writing to logs...");
			e.printStackTrace();
			try{
				File errLog = new File("error_log_client" + System.currentTimeMillis()%100000000 + ".log");
				PrintWriter pw = new PrintWriter(errLog);
				e.printStackTrace(pw);
				pw.close();
			}catch (IOException e2){
				e2.printStackTrace();
			}
			System.exit(0);
		}
	}
	
	
	
	public void init(int width, int height, String name) {
		super.init(width, height, name);
		Player p1 = new Player("Player", (byte) 0);
		localPlayer = p1;
		ByteBuffer icon16, icon32;
		icon16 = icon32 = null;
		try {
			icon16 = ByteBuffer.wrap(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/gui/icon16.png")).getTextureData());
			icon32 = ByteBuffer.wrap(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/gui/icon32.png")).getTextureData());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Display.setIcon(new ByteBuffer[]{icon16, icon32});
		FEResources.loadResources();
		FEResources.loadBitmapFonts();
		WeaponFactory.loadWeapons();
		UnitFactory.loadUnits();
		p1.getParty().setColor(Party.TEAM_BLUE);
		
		/* OpenGL final setup */
		glEnable(GL_LINE_SMOOTH);
		
		UnitFactory.getUnit("Lyn");
		connect = new ConnectStage();
		setCurrentStage(new TitleStage());
		messages = new ArrayList<Message>();
		SoundTrack.loop("main_theme");
		
	}
	
	public void testDraftStage() {
		Player p1 = localPlayer;
		testSession = new Session();
		testSession.setMaxUnits(6);
		Player p2 = new Player("p2", (byte) 1);
		p2.getParty().setColor(Party.TEAM_RED);
		p2.getParty().addUnit(UnitFactory.getUnit("Mia"));
		p2.getParty().addUnit(UnitFactory.getUnit("L'Arachel"));
		testSession.addPlayer(p1);
		testSession.addPlayer(p2);
		currentStage = new TeamDraftStage(testSession);
	}
	
	public void testFightStage(){
		Player p1 = localPlayer;
		testSession = new Session();
		testSession.setMap("test");
		testSession.setObjective(new Seize());
		Player p2 = new Player("p2", (byte) 1);
		p2.getParty().setColor(Party.TEAM_RED);
		testSession.addPlayer(p1);
		testSession.addPlayer(p2);
		
		map = new ClientOverworldStage(testSession);
		Unit u1 = UnitFactory.getUnit("Gilliam");
		u1.getInventory().add(WeaponFactory.getWeapon("Gradivus"));
		map.addUnit(u1, 0, 0);
		u1.equip(0);
		u1.setLevel(20);
		u1.loadMapSprites();
		p1.getParty().addUnit(u1);
		
		Unit u2 = UnitFactory.getUnit("Joshua");
		u2.getInventory().add(WeaponFactory.getWeapon("Wo Dao"));
		map.addUnit(u2, 1, 0);
		u2.equip(0);
		u2.setLevel(15);
		u2.loadMapSprites();
		p2.getParty().addUnit(u2);
		
		map.processAddStack();
		int u2Uses = u2.getWeapon().getMaxUses();
		CombatCalculator calc = new CombatCalculator(new UnitIdentifier(u1), new UnitIdentifier(u2), true);
		System.out.println(calc.getAttackQueue());
		
		u1.getInventory().add(WeaponFactory.getWeapon("Debug Noodle"));
		u1.equip(0);
		u2.getWeapon().setUsesDEBUGGING(u2Uses);
		u1.fillHp();
		u2.fillHp();
		setCurrentStage(new FightStage(new UnitIdentifier(u1), new UnitIdentifier(u2), calc.getAttackQueue()));
	}
	
	public void testOverworldStage() {
		testSession = new Session();
		testSession.setMap("test");
		testSession.setObjective(new Seize());
		testSession.addPlayer(localPlayer);
		
		Player p2 = new Player("P2", (byte)1);
		p2.getParty().setColor(Party.TEAM_RED);
		testSession.addPlayer(p2);
		
		Unit u1 = UnitFactory.getUnit("Gilliam");
		u1.addToInventory(WeaponFactory.getWeapon("Gradivus"));
		u1.equip(0);
		u1.setHp(1);
		localPlayer.getParty().addUnit(u1);
		
		Unit u3 = UnitFactory.getUnit("Joshua");
		u3.addToInventory(WeaponFactory.getWeapon("Wo Dao"));
		u3.equip(0);
		u3.setHp(1);
		p2.getParty().addUnit(u3);
		
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
		if(client.isOpen()) {
			lobby = new ClientLobbyStage(client.getSession());
			setCurrentStage(lobby);
			client.start();
		} else {
			currentStage.addEntity(new Notification(
					180, 120, "default_med", "ERROR: Could not connect to the server!", 5f, new Color(255, 100, 100), 0f));
		}
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
//				FEResources.getBitmapFont("stat_numbers").render(
//						(int)(1.0f/getDeltaSeconds())+"", 440f, 0f, 0f);
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
