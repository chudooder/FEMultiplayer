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
		HashMap<String, Integer> lynBases = new HashMap<String, Integer>();
		lynBases.put("Lvl", 1);
		lynBases.put("HP", 18);
		lynBases.put("Str", 5);
		lynBases.put("Mag", 5);
		lynBases.put("Spd", 200);
		lynBases.put("Skl", 7);
		lynBases.put("Lck", 7);
		lynBases.put("Def", 5);
		lynBases.put("Res", 0);
		lynBases.put("Con", 9);
		lynBases.put("Mov", 7);

		HashMap<String, Integer> lynGrowths = new HashMap<String, Integer>();
		lynGrowths.put("HP", 80);
		lynGrowths.put("Str", 45);
		lynGrowths.put("Mag", 0);
		lynGrowths.put("Skl", 40);
		lynGrowths.put("Spd", 40);
		lynGrowths.put("Def", 30);
		lynGrowths.put("Res", 35);
		lynGrowths.put("Lck", 45);
		

		HashMap<String, Integer> luteBases = new HashMap<String, Integer>();
		luteBases.put("Lvl", 1);
		luteBases.put("HP", 18);
		luteBases.put("Str", 5);
		luteBases.put("Mag", 5);
		luteBases.put("Skl", 7);
		luteBases.put("Spd", 7);
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

		Unit eliwood = new Unit("Lyn", Class.createClass("Lyn"), lynBases,
				lynGrowths);
		eliwood.addToInventory(WeaponFactory.getWeapon("Steel Sword"));
		eliwood.equip(0);
		blue.addUnit(eliwood);

		Unit lute = new Unit("Eliwood", Class.createClass("Eliwood"), luteBases, luteGrowths);
		lute.addToInventory(WeaponFactory.getWeapon("Durandal"));
		lute.equip(0);
		red.addUnit(lute);

		eliwood.setLevel(40);
		lute.setLevel(40);
		
		
		eliwood.fillHp();
		lute.fillHp();
		
		OverworldStage map = new OverworldStage(new Grid(10,10, Terrain.PLAIN));
		map.addUnit(eliwood, 0, 0);
		map.addUnit(lute, 1, 0);
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
