package net.fe;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;

import net.fe.fightStage.FightStage;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.Terrain;
import net.fe.unit.Class;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;
import net.fe.unit.WeaponFactory;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.Renderer;
import chu.engine.network.Client;
import chu.engine.network.Message;

public class FEMultiplayer extends Game{
	private static Stage currentStage;
	private static Client client;
	private static ArrayList<Message> serverMessages;
	
	public static void main(String[] args) {
		FEMultiplayer game = new FEMultiplayer();
		game.init(640, 480, "ice baadgee");
		game.loop();
	}
	
	public void init(int width, int height, String name) {
		super.init(width, height, name);
		//TODO: Implement client
//		client = new Client();
		/* OpenGL final setup */
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		// TODO: Beta testing stuff, delete later
		HashMap<String, Integer> eliwoodBases = new HashMap<String, Integer>();
		eliwoodBases.put("Lvl", 1);
		eliwoodBases.put("HP", 18);
		eliwoodBases.put("Str", 5);
		eliwoodBases.put("Mag", 5);
		eliwoodBases.put("Spd", 99);
		eliwoodBases.put("Skl", 7);
		eliwoodBases.put("Lck", 7);
		eliwoodBases.put("Def", 5);
		eliwoodBases.put("Res", 0);
		eliwoodBases.put("Con", 9);
		eliwoodBases.put("Mov", 7);

		HashMap<String, Integer> eliwoodGrowths = new HashMap<String, Integer>();
		eliwoodGrowths.put("HP", 80);
		eliwoodGrowths.put("Str", 45);
		eliwoodGrowths.put("Mag", 0);
		eliwoodGrowths.put("Skl", 40);
		eliwoodGrowths.put("Spd", 40);
		eliwoodGrowths.put("Def", 30);
		eliwoodGrowths.put("Res", 35);
		eliwoodGrowths.put("Lck", 45);

		HashMap<String, Integer> royBases = new HashMap<String, Integer>();
		royBases.put("Lvl", 1);
		royBases.put("HP", 18);
		royBases.put("Str", 5);
		royBases.put("Mag", 5);
		royBases.put("Skl", 5);
		royBases.put("Spd", 7);
		royBases.put("Lck", 7);
		royBases.put("Def", 5);
		royBases.put("Res", 0);
		royBases.put("Con", 8);
		royBases.put("Mov", 6);

		HashMap<String, Integer> royGrowths = new HashMap<String, Integer>();
		royGrowths.put("HP", 85);
		royGrowths.put("Str", 45);
		royGrowths.put("Mag", 0);
		royGrowths.put("Skl", 55);
		royGrowths.put("Spd", 40);
		royGrowths.put("Def", 30);
		royGrowths.put("Res", 35);
		royGrowths.put("Lck", 0);
		
		Party blue = new Party();
		blue.setColor(Party.TEAM_BLUE);
		Party red = new Party();
		red.setColor(Party.TEAM_RED);

		Unit eliwood = new Unit("Eliwood", Class.createClass("Eliwood"), eliwoodBases,
				eliwoodGrowths);
		eliwood.addToInventory(WeaponFactory.getWeapon("Steel Lance"));
		eliwood.equip(0);
		blue.addUnit(eliwood);

		Unit roy = new Unit("Roy", Class.createClass("Roy"), royBases, royGrowths);
		roy.addToInventory(WeaponFactory.getWeapon("Tree Branch"));
		roy.equip(0);
		red.addUnit(roy);

		eliwood.setLevel(40);
		roy.setLevel(40);
		
		
		eliwood.fillHp();
		roy.fillHp();
		
		OverworldStage map = new OverworldStage(new Grid(10,10, Terrain.PLAIN));
		map.addUnit(eliwood, 0, 0);
		map.addUnit(roy, 0, 1);
		map.processAddStack();
		currentStage = new FightStage(eliwood, roy);
		serverMessages = new ArrayList<Message>();
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
	
	public static void setCurrentStage(Stage stage) {
		currentStage = stage;
	}

}
