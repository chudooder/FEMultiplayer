package net.fe.builderStage;

import java.util.ArrayList;
import java.util.List;

import net.fe.Button;
import net.fe.ControlsDisplay;
import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.RunesBg;
import net.fe.Session;
import net.fe.fightStage.FightStage;
import net.fe.overworldStage.InventoryMenu;
import net.fe.overworldStage.UnitInfo;
import net.fe.unit.HealingItem;
import net.fe.unit.Item;
import net.fe.unit.MapAnimation;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;

public class UnitBuilderStage extends Stage {
	private Unit unit;
	private ShopMenu shop;
	private InventoryMenu inv;
	private float[] repeatTimers = new float[4];
	private TeamBuilderStage back;
	private State state;
	private Button levelUp, levelDown;
	private ControlsDisplay controls;
	
	//CONFIG
	public static final int
	INVENTORY_X = 30, INVENTORY_Y = 115, SHOP_X = 335, SHOP_Y = 20, 
	LEVEL_X = 175, LEVEL_Y = 115,
	INFO_X = 7, INFO_Y = 236, INFO_W = 316, INFO_H = 56;
	
	public UnitBuilderStage(Unit u, TeamBuilderStage s, Session session){
		super("preparations");
		addEntity(new RunesBg(new Color(0xd2b48c)));
		back = s;
		unit = u;
		inv = new InventoryMenu(u, INVENTORY_X, INVENTORY_Y){{
			drawCost = true;
			setWidth(135);
		}};
		addEntity(inv);
		
		controls = new ControlsDisplay();
		controls.addControl("Z", "Buy");
		controls.addControl("X", "Back");
		addEntity(controls);
		
		UnitInfo ui = new UnitInfo(5,5);
		ui.setUnit(u);
		addEntity(ui);
		
		shop = new ShopMenu(SHOP_X, SHOP_Y, session);
		shop.clearSelection();
		
		addEntity(shop);
		
		levelUp = new Button(LEVEL_X, LEVEL_Y, "Level Up", Color.green, 135){
			public void onStep(){
				String exp =  Unit.getExpCost(unit.get("Lvl") + 1)+"";
				if(unit.get("Lvl") == 20)
					exp = "--";
				text = "Level Up: " +exp + " EXP";
			}
			public void execute() {
				if(unit.get("Lvl") != 20){
					int cost = Unit.getExpCost(unit.get("Lvl") + 1);
					if(cost <= back.getExp()){
						unit.setLevel(unit.get("Lvl") + 1);
						back.setExp(back.getExp() - cost);
					}
				}
			}
		};
		levelDown = new Button(LEVEL_X, LEVEL_Y + 24, "Level Down", Color.red, 135){
			public void onStep(){
				String exp =  Unit.getExpCost(unit.get("Lvl"))+"";
				if(unit.get("Lvl") == 1)
					exp = "--";
				text = "Level Down: " + exp + " EXP";
			}
			public void execute() {
				if(unit.get("Lvl") != 1){
					int cost = Unit.getExpCost(unit.get("Lvl"));
					unit.setLevel(unit.get("Lvl")-1);
					back.setExp(back.getExp() + cost);
				}
			}
		};;
		
		addEntity(levelUp);
		addEntity(levelDown);
		
		state = new Normal();
	}
	
	public void render(){
		super.render();
		Renderer.drawString("default_med", "Items", INVENTORY_X, INVENTORY_Y - 14, 0);
		String funds = "Funds: " + back.getFunds();
		int width = FEResources.getBitmapFont("default_med").getStringWidth(funds);
		Renderer.drawString("default_med", funds, INVENTORY_X+135-width, INVENTORY_Y + 75, 0);
		String exp = "EXP: " + back.getExp();
		width = FEResources.getBitmapFont("default_med").getStringWidth(exp);
		Renderer.drawString("default_med", "EXP: " + back.getExp(), LEVEL_X+135-width, LEVEL_Y + 50, 0);
		state.render();
	}
	
	@Override
	public void beginStep() {
		for (Entity e : entities) {
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
		MapAnimation.updateAll();
		List<KeyboardEvent> keys = Game.getKeys();
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && repeatTimers[0] == 0) {
			repeatTimers[0] = 0.15f;
			state.up();
			state.updateControls();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && repeatTimers[1] == 0) {
			repeatTimers[1] = 0.15f;
			state.down();
			state.updateControls();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && repeatTimers[2] == 0) {
			repeatTimers[2] = 0.15f;
			state.left();
			state.updateControls();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && repeatTimers[3] == 0) {
			repeatTimers[3] = 0.15f;
			state.right();
			state.updateControls();
		}
		for(KeyboardEvent ke : keys) {
			if(ke.state) {
				if(ke.key == Keyboard.KEY_Z) {
					state.select();
					state.updateControls();
				} else if (ke.key == Keyboard.KEY_X){
					state.cancel();
					state.updateControls();
				}
					
			}
		}
	
		for(int i=0; i<repeatTimers.length; i++) {
			if(repeatTimers[i] > 0) {
				repeatTimers[i] -= Game.getDeltaSeconds();
				if(repeatTimers[i] < 0) repeatTimers[i] = 0;
			}
		}
		
	}

	@Override
	public void onStep() {
		for (Entity e : entities) {
			e.onStep();
		}
		processAddStack();
		processRemoveStack();
		
	}

	@Override
	public void endStep() {
		for (Entity e : entities) {
			e.onStep();
		}
		processAddStack();
		processRemoveStack();
	}
	
	public void renderItem(Item i){
		Renderer.drawString("default_med", i.name, INFO_X + 2, INFO_Y + 4, 1);
		if(i instanceof HealingItem){
			HealingItem potion = (HealingItem) i;
			Renderer.drawString("default_med", "Heals " + potion.amount + " HP", INFO_X+8, INFO_Y+28, 1);
		} else if (shop.getItem() instanceof Weapon){
			Weapon wep = (Weapon) i;
			Renderer.drawString("default_med", "Mt " + wep.mt, INFO_X+8, INFO_Y+20, 1);
			Renderer.drawString("default_med", "Hit " + wep.hit, INFO_X+68, INFO_Y+20, 1);
			Renderer.drawString("default_med", "Crit " + wep.crit, INFO_X+128, INFO_Y+20, 1);
			int rngmin = 255;
			int rngmax = 0;
			for(int rng: wep.range){
				if(rng < rngmin) rngmin = rng;
				if(rng > rngmax) rngmax = rng;
			}
			if(rngmin == rngmax){
				Renderer.drawString("default_med", "Rng " + rngmin, INFO_X+ 188, INFO_Y+20, 1);
			} else {
				Renderer.drawString("default_med", "Rng " + rngmin + "-" + rngmax, INFO_X+ 188, INFO_Y+20, 1);
			}
			
			ArrayList<String> flavor = new ArrayList<String>();
			if(wep.name.contains("Brave")){
				flavor.add("Allows double attacks");
			}
			if(wep.name.contains("reaver")){
				flavor.add("Reverses the weapon triangle");
			}
			if(wep.name.contains("Kill") || wep.name.equals("Wo Dao")){
				flavor.add("Has a high critical rate");
			}
			if(wep.getCost() == 10000){
				flavor.add("A legendary weapon");
			}
			if(wep.getCost() == 15000){
				flavor.add("Ultimate magic");
			}
			if(wep.pref != null) flavor.add(wep.pref + " only");
			for(String stat: wep.modifiers.keySet()){
				if(wep.modifiers.get(stat) != 0){
					flavor.add(stat + "+" + wep.modifiers.get(stat));
				}
			}
			if(wep.effective.size() != 0){
				ArrayList<String> eff = new ArrayList<String>();
				if(wep.effective.contains("General")){
					eff.add("armored");
				} 
				if(wep.effective.contains("Valkyrie")){
					eff.add("mounted");
				}
				if(wep.effective.contains("Falconknight") && 
						!wep.effective.contains("Valkyrie")){
					eff.add("flying");
				}
				String effText = "";
				for(String s: eff){
					effText += ", " + s;
				}
				if(effText.length() != 0)
					flavor.add("Effective against " + effText.substring(2) + " units");
			}
			
			if(flavor.size() != 0){
				String flavorText = "";
				for(String s: flavor){
					flavorText += s + ". ";
				}
				Renderer.drawString("default_med", flavorText, INFO_X+8, INFO_Y+36, 1);
			}
		}
	}
	
	private abstract class State{
		public abstract void up();
		public abstract void down();
		public abstract void left();
		public abstract void right();
		public abstract void select();
		public abstract void cancel();
		public abstract void render();
		public abstract void updateControls();
	}
	
	private class Normal extends State {
		@Override
		public void up() {
			AudioPlayer.playAudio("cursor2", 1, 1);
			if(inv.hasSelection()){
				if(inv.getSelectedIndex() == 0){
					inv.clearSelection();
					levelDown.setHover(true);
				} else {
					inv.up();
				}
			} else if (levelUp.hovered()){
				inv.restoreSelection();
				inv.setSelection(inv.size() - 1);
				levelUp.setHover(false);
			} else {
				levelDown.setHover(false);
				levelUp.setHover(true);
			}
		}

		@Override
		public void down() {
			AudioPlayer.playAudio("cursor2", 1, 1);
			if(inv.hasSelection()){
				if(inv.getSelectedIndex() == inv.size() - 1){
					inv.clearSelection();
					levelUp.setHover(true);
				} else {
					inv.down();
				}
			} else if (levelDown.hovered()){
				inv.restoreSelection();
				inv.setSelection(0);
				levelDown.setHover(false);
			} else {
				levelDown.setHover(true);
				levelUp.setHover(false);
			}
		}

		@Override
		public void left() {
			// TODO Auto-generated method stub	
		}

		@Override
		public void right() {
			// TODO Auto-generated method stub
		}

		@Override
		public void select() {
			
			if(levelUp.hovered()){
				AudioPlayer.playAudio("select", 1, 1);
				levelUp.execute();
			} else if(levelDown.hovered()){
				AudioPlayer.playAudio("select", 1, 1);
				levelDown.execute();
			} else {
				if(inv.getSelection() != null){
					AudioPlayer.playAudio("cancel", 1, 1);
					Item i = inv.getSelection().getItem();
					if(!(i instanceof Weapon && ((Weapon) i).pref != null)){
						back.setFunds(back.getFunds() + i.getCost());
						unit.removeFromInventory(i);
					}
				} else {
					AudioPlayer.playAudio("select", 1, 1);
					inv.clearSelection();
					shop.restoreSelection();
					state = new Shop();
				}
			}
		}

		@Override
		public void cancel() {
			AudioPlayer.playAudio("cancel", 1, 1);
			FEMultiplayer.setCurrentStage(back);
		}
		
		public void render(){
			Renderer.drawBorderedRectangle(INFO_X, INFO_Y, INFO_X+INFO_W, INFO_Y+INFO_H, 1,
					FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
			if(inv.getSelectedIndex() < inv.size() && inv.getSelection() != null){
				renderItem(inv.getSelection().getItem());
			} else {
				Renderer.drawString("default_med", unit.getTheClass().name, INFO_X+2, INFO_Y+4, 1);
				Renderer.drawString("default_med", 
						"Skill: " + unit.getTriggers().get(0).getClass().getSimpleName(), 
						INFO_X+228, INFO_Y+4, 1);
				Renderer.drawString("default_med", unit.getTheClass().description, INFO_X+8, INFO_Y+20, 1);
				ArrayList<String> weps = new ArrayList<String>();
				for(Weapon.Type type: unit.getTheClass().usableWeapon){
					if(type.isMagic()){
						weps.add(type.toString().toLowerCase() + " magic");
					} else if (type.equals(Weapon.Type.STAFF)){
						weps.add("staves");
					} else {
						weps.add(type.toString().toLowerCase() + "s");
					}
				}
				String wepText = "";
				for(String s: weps){
					wepText += ", " + Character.toUpperCase(s.charAt(0)) + s.substring(1);
				}
				Renderer.drawString("default_med", "Equips: " + wepText.substring(2), INFO_X+8, INFO_Y+36, 1);
			}
		}
		
		public void updateControls(){
			controls.set("X", "Back");
			if(levelUp.hovered() || levelDown.hovered()){
				controls.set("Z", "Select");
			} else if (inv.getSelection() == null){
				controls.set("Z", "Buy");
			} else {
				controls.set("Z", "Sell");
			}
		}
		
	}
	
	private class Shop extends State{
		@Override
		public void up() {
			AudioPlayer.playAudio("cursor2", 1, 1);
			shop.up();
		}

		@Override
		public void down() {
			AudioPlayer.playAudio("cursor2", 1, 1);
			shop.down();
		}

		@Override
		public void left() {
			AudioPlayer.playAudio("sword_swipe2", 1, 1);
			shop.left();
		}

		@Override
		public void right() {
			AudioPlayer.playAudio("sword_swipe2", 1, 1);
			shop.right();
		}

		@Override
		public void select() {
			Item i = shop.getItem();
			if(i.getCost() <= back.getFunds()){
				back.setFunds(back.getFunds() - i.getCost());
				unit.addToInventory(i);
				cancel();
			}
		}

		@Override
		public void cancel() {
			AudioPlayer.playAudio("cancel", 1, 1);
			inv.restoreSelection();
			shop.clearSelection();
			state = new Normal();
		}
		
		public void render(){
			Renderer.drawBorderedRectangle(INFO_X, INFO_Y, INFO_X+INFO_W, INFO_Y+INFO_H, 1,
					FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
			renderItem(shop.getItem());
		}
		
		public void updateControls(){
			controls.set("Z", "Buy");
			controls.set("X", "Cancel");
		}
		
	}

	public Session getSession() {
		return back.getSession();
	}
	

}




