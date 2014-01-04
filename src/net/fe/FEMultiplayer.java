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
import net.fe.unit.HealingItem;
import net.fe.unit.Unit;
import net.fe.unit.UnitFactory;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.WeaponFactory;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
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
		
		Unit u4 = UnitFactory.getUnit("Lute");
		u4.addToInventory(WeaponFactory.getWeapon("Fimbulvetr"));
		u4.addToInventory(WeaponFactory.getWeapon("Elfire"));
		u4.addToInventory(WeaponFactory.getWeapon("Heal"));
		u4.equipFirstWeapon(1);
		p1.getParty().addUnit(u4);
		
		Unit u5 = UnitFactory.getUnit("Roy");
		u5.equipFirstWeapon(1);
		p1.getParty().addUnit(u5);
		
		Unit u6 = UnitFactory.getUnit("Hector");
		u6.addToInventory(WeaponFactory.getWeapon("Wo Dao"));
		u6.equipFirstWeapon(1);
		p2.getParty().addUnit(u6);
		
		Unit u7 = UnitFactory.getUnit("Eirika");
		u7.equipFirstWeapon(1);
		p1.getParty().addUnit(u7);
		
		Unit u8 = UnitFactory.getUnit("Ephraim");
		u8.addToInventory(WeaponFactory.getWeapon("Debug Javelin"));
		u8.equipFirstWeapon(1);
		p1.getParty().addUnit(u8);
		
		Unit u9 = UnitFactory.getUnit("Matthew");
		u9.addToInventory(WeaponFactory.getWeapon("Noodle"));
		u9.addToInventory(WeaponFactory.getWeapon("Killing Edge"));
		u9.equipFirstWeapon(1);
		p2.getParty().addUnit(u9);
		
		Unit u10 = UnitFactory.getUnit("Gilliam");
		u10.addToInventory(WeaponFactory.getWeapon("Iron Axe"));
		u10.addToInventory(WeaponFactory.getWeapon("Horseslayer"));
		u10.equipFirstWeapon(1);
		p1.getParty().addUnit(u10);
		
		Unit u11 = UnitFactory.getUnit("Raven");
		u11.addToInventory(WeaponFactory.getWeapon("Killer Axe"));
		u11.addToInventory(WeaponFactory.getWeapon("Tree Branch"));
		u11.addToInventory(WeaponFactory.getWeapon("Debug Sword"));
		u11.equipFirstWeapon(1);
		p1.getParty().addUnit(u11);
		
		Unit u12 = UnitFactory.getUnit("Dart");
		u12.addToInventory(WeaponFactory.getWeapon("Debug Axe"));
		u12.addToInventory(WeaponFactory.getWeapon("Iron Axe"));
		u12.addToInventory(WeaponFactory.getWeapon("Hand Axe"));
		u12.equipFirstWeapon(1);
		p1.getParty().addUnit(u12);
		
		Unit u13 = UnitFactory.getUnit("Kent");
		u13.addToInventory(WeaponFactory.getWeapon("Horseslayer"));
		u13.addToInventory(WeaponFactory.getWeapon("Debug Lance"));
		u13.addToInventory(WeaponFactory.getWeapon("Iron Sword"));
		u13.addToInventory(WeaponFactory.getWeapon("Debug Sword"));
		u13.equipFirstWeapon(1);
		p1.getParty().addUnit(u13);
		
		Unit u14 = UnitFactory.getUnit("Joshua");
		u14.addToInventory(WeaponFactory.getWeapon("Iron Sword"));
		u14.addToInventory(WeaponFactory.getWeapon("Debug Sword"));
//		u14.equipFirstWeapon(1);
		p2.getParty().addUnit(u14);
		
		
		

		u1.setLevel(20);
		u2.setLevel(20);
		u3.setLevel(1);
		u4.setLevel(20);
		u5.setLevel(20);
		u6.setLevel(1);
		u7.setLevel(20);
		u8.setLevel(20);
		u9.setLevel(20);
		u10.setLevel(20);
		u11.setLevel(20);
		u12.setLevel(20);
		u13.setLevel(20);
		u14.setLevel(20);
		
		u1.fillHp();
		u1.setHp(1);
		u2.fillHp();
		u3.fillHp();
		u4.fillHp();
		u5.fillHp();
		u6.fillHp();
		u7.fillHp();
		u8.fillHp();
		u9.fillHp();
		u10.fillHp();
		u11.fillHp();
		u12.fillHp();
		u13.fillHp();
		u14.fillHp();
		
		map = new OverworldStage("test", p1);
		map.addUnit(u1, 0, 0);
		map.addUnit(u2, 2, 1);
		map.addUnit(u3, 1, 1);
		map.addUnit(u4, 2, 0);
		map.addUnit(u5, 2, 3);
		map.addUnit(u6, 4, 2);
		map.addUnit(u7, 8, 3);
		map.addUnit(u8, 2, 2);
		map.addUnit(u9, 3, 6);
		map.addUnit(u10, 3, 4);
		map.addUnit(u11, 4, 0);
		map.addUnit(u12, 5, 2);
		map.addUnit(u13, 5, 3);
		map.addUnit(u14, 0, 1);
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
//		client.close();
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
