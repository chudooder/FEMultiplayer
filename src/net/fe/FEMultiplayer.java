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

import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.fightStage.HealCalculator;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.Terrain;
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
import chu.engine.anim.Renderer;
import chu.engine.network.Client;
import chu.engine.network.Message;

public class FEMultiplayer extends Game{
	private static Stage currentStage;
	private static Client client;
	private static ArrayList<Message> serverMessages;
	
	private static ArrayList<Player> players;
	
	public static Player turn;
	public static OverworldStage map;
	
	public static void main(String[] args) {
		FEMultiplayer game = new FEMultiplayer();
		game.init(480, 320, "Fire Emblem Multiplayer");
		game.loop();
		
	}
	
	public void init(int width, int height, String name) {
		super.init(width, height, name);
		players = new ArrayList<Player>();
		Player p1 = new Player((byte) 0);
		Player p2 = new Player((byte) 1);
		players.add(p1);
		players.add(p2);
		p1.getParty().setColor(Party.TEAM_BLUE);
		p2.getParty().setColor(Party.TEAM_RED);
		
		//TODO: Implement client
//		client = new Client();
		/* OpenGL final setup */
		glEnable(GL_LINE_SMOOTH);
		
		
		
		

		Unit u1 = UnitFactory.getUnit("Lyn");
		u1.addToInventory(WeaponFactory.getWeapon("Brave Sword"));
		u1.equip(1);
		p1.getParty().addUnit(u1);
		
		Unit u3 = UnitFactory.getUnit("Erk");
		u3.addToInventory(WeaponFactory.getWeapon("Elfire"));
		u3.addToInventory(WeaponFactory.getWeapon("Physic"));
		u3.equip(1);
		p1.getParty().addUnit(u3);

		Unit u2 = UnitFactory.getUnit("Eliwood");
		u2.addToInventory(WeaponFactory.getWeapon("Brave Lance"));
		u2.equip(1);
		p2.getParty().addUnit(u2);
		
		Unit u4 = UnitFactory.getUnit("Lute");
		u4.addToInventory(WeaponFactory.getWeapon("Elfire"));
		u4.addToInventory(WeaponFactory.getWeapon("Heal"));
		u4.equip(0);
		p2.getParty().addUnit(u4);
		
		Unit u5 = UnitFactory.getUnit("Roy");
		u5.equip(0);
		p1.getParty().addUnit(u5);

		u1.setLevel(20);
		u2.setLevel(20);
		u3.setLevel(20);
		u4.setLevel(20);
		u5.setLevel(20);
		
		u1.fillHp();
		u2.fillHp();
		u3.fillHp();
		u4.fillHp();
		
		map = new OverworldStage(new Grid(20,10, Terrain.PLAIN), p1);
		map.addUnit(u1, 0, 0);
		map.addUnit(u2, 3, 3);
		map.addUnit(u3, 1, 1);
		map.addUnit(u4, 2, 0);
		map.addUnit(u5, 2, 3);
		map.setControl(true);
		
		
		currentStage = map;
		serverMessages = new ArrayList<Message>();
		
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
			//TODO: Client
//			serverMessages.clear();
//			serverMessages.addAll(client.getMessages());
//			for(Message m : serverMessages)
//				client.messages.remove(m);
			SoundStore.get().poll(0);
			glPushMatrix();
			if(!paused) {
				currentStage.beginStep();
				currentStage.onStep();
				currentStage.processAddStack();
				currentStage.processRemoveStack();
				Renderer.getCamera().lookThrough();
				currentStage.render();
				currentStage.endStep();
			}
			glPopMatrix();
			Display.update();
			timeDelta = System.nanoTime()-time;
		}
		AL.destroy();
		Display.destroy();
//		client.close();
	}
	
	public static void reportFightResults(FightStage stage){ //TODO get parameters that make sense
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
		
		//Debugging
		if(cmds.length < 2) return;
		if(cmds[cmds.length - 2].equals("Attack")){
			UnitIdentifier enemy = (UnitIdentifier) cmds[cmds.length-1];
			CombatCalculator calc = new CombatCalculator(u, enemy);
			FightStage to = new FightStage(u, enemy, calc.getAttackQueue());
			currentStage.addEntity(new OverworldFightTransition(to, u, enemy));
		} else if (cmds[cmds.length - 2].equals("Heal")){
			UnitIdentifier healee = (UnitIdentifier) cmds[cmds.length-1];
			HealCalculator calc = new HealCalculator(u, healee);
			FightStage to = new FightStage(u, healee, calc.getAttackQueue());
			currentStage.addEntity(new OverworldFightTransition(to, u, healee));
		}
	}

	
	public static void setCurrentStage(Stage stage) {
		currentStage = stage;
	}

	public static Stage getOverworldStage() {
		return map;
	}

}
