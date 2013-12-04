package net.fe;

import static org.lwjgl.opengl.GL11.*;

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
		game.init(480, 320, "Fire Emblem Multiplayer");
		game.loop();
	}
	
	public void init(int width, int height, String name) {
		super.init(width, height, name);
		//TODO: Implement client
//		client = new Client();
		/* OpenGL final setup */
		glEnable(GL_LINE_SMOOTH);
		// TODO: Beta testing stuff, delete later
		HashMap<String, Integer> eliwoodBases = new HashMap<String, Integer>();
		eliwoodBases.put("Lvl", 1);
		eliwoodBases.put("HP", 18);
		eliwoodBases.put("Str", 5);
		eliwoodBases.put("Mag", 5);
		eliwoodBases.put("Spd", 7);		//orig 7
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
		

		HashMap<String, Integer> luteBases = new HashMap<String, Integer>();
		luteBases.put("Lvl", 1);
		luteBases.put("HP", 18);
		luteBases.put("Str", 5);
		luteBases.put("Mag", 5);
		luteBases.put("Skl", 200);
		luteBases.put("Spd", 7);		//orig 7
		luteBases.put("Lck", 7);
		luteBases.put("Def", 5);
		luteBases.put("Res", 0);
		luteBases.put("Con", 8);
		luteBases.put("Mov", 6);

		HashMap<String, Integer> luteGrowths = new HashMap<String, Integer>();
		luteGrowths.put("HP", 85);
		luteGrowths.put("Str", 0);
		luteGrowths.put("Mag", 60);
		luteGrowths.put("Skl", 55);
		luteGrowths.put("Spd", 40);
		luteGrowths.put("Def", 30);
		luteGrowths.put("Res", 35);
		luteGrowths.put("Lck", 0);
		
		Party blue = new Party();
		blue.setColor(Party.TEAM_BLUE);
		Party red = new Party();
		red.setColor(Party.TEAM_RED);

		Unit eliwood = new Unit("Eliwood", Class.createClass("Eliwood"), eliwoodBases,
				eliwoodGrowths);
		eliwood.addToInventory(WeaponFactory.getWeapon("Durandal"));
		eliwood.equip(0);
		blue.addUnit(eliwood);

		Unit lute = new Unit("Lute", Class.createClass("Sage"), luteBases, luteGrowths);
		lute.addToInventory(WeaponFactory.getWeapon("Elfire"));
		lute.equip(0);
		red.addUnit(lute);

		eliwood.setLevel(40);
		lute.setLevel(40);
		
		
		eliwood.fillHp();
		lute.fillHp();
		
		OverworldStage map = new OverworldStage(new Grid(10,10, Terrain.PLAIN));
		map.addUnit(eliwood, 0, 0);
		map.addUnit(lute, 1, 1);
		map.processAddStack();
		currentStage = new FightStage(eliwood, lute);
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
