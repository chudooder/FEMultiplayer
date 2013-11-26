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
		HashMap<String, Float> stats1 = new HashMap<String, Float>();
		stats1.put("Skl", 10f);
		stats1.put("Lck", 1f);
		stats1.put("HP", 15f);
		stats1.put("Str", 10f);
		stats1.put("Mag", 10f);
		stats1.put("Def", 10f);
		stats1.put("Res", 10f);
		stats1.put("Spd", 12f);
		stats1.put("Lvl", 1f);
		stats1.put("Mov", 3f);

		HashMap<String, Integer> growths1 = new HashMap<String, Integer>();
		growths1.put("HP", 70);
		growths1.put("Str", 50);
		growths1.put("Mag", 10);
		growths1.put("Skl", 70);
		growths1.put("Spd", 70);
		growths1.put("Def", 40);
		growths1.put("Res", 30);
		growths1.put("Lck", 60);

		HashMap<String, Float> stats2 = new HashMap<String, Float>();
		stats2.put("Skl", 10f);
		stats2.put("Lck", 3f);
		stats2.put("HP", 15f);
		stats2.put("Str", 10f);
		stats2.put("Mag", 10f);
		stats2.put("Def", 10f);
		stats2.put("Res", 10f);
		stats2.put("Spd", 8f);
		stats2.put("Lvl", 1f);
		stats2.put("Mov", 3f);

		HashMap<String, Integer> growths2 = new HashMap<String, Integer>();
		growths2.put("HP", 70);
		growths2.put("Str", 60);
		growths2.put("Mag", 10);
		growths2.put("Skl", 60);
		growths2.put("Spd", 50);
		growths2.put("Def", 40);
		growths2.put("Res", 30);
		growths2.put("Lck", 60);

		Unit marth = new Unit("Marth", Class.createClass("Assassin"), stats1,
				growths1);
		marth.addToInventory(Weapon.createWeapon("sord"));
		marth.equip(0);

		Unit roy = new Unit("Roy", Class.createClass("Paladin"), stats2, growths2);
		roy.addToInventory(Weapon.createWeapon("lunce"));
		roy.equip(0);

		for (int i = 0; i < 15; i++) {
			marth.levelUp();
			roy.levelUp();
		}
		OverworldStage map = new OverworldStage(new Grid(10,10, Terrain.PLAIN));
		map.addUnit(marth, 0, 0);
		map.addUnit(roy, 0, 1);
		map.processAddStack();
		currentStage = new FightStage(marth, roy);
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
