package net.fe.builderStage;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import net.fe.Button;
import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.overworldStage.InventoryMenu;
import net.fe.overworldStage.ItemMenu;
import net.fe.overworldStage.UnitInfo;
import net.fe.unit.Item;
import net.fe.unit.ItemDisplay;
import net.fe.unit.MapAnimation;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;
import net.fe.unit.WeaponFactory;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class UnitBuilderStage extends Stage {
	private Unit unit;
	private ShopMenu shop;
	private InventoryMenu inv;
	private float[] repeatTimers = new float[4];
	private TeamBuilderStage back;
	private State state;
	private Button levelUp, levelDown;
	
	//CONFIG
	public static final int
	INVENTORY_X = 100, INVENTORY_Y = 115, SHOP_X = 335, SHOP_Y = 20;
	
	public UnitBuilderStage(Unit u, TeamBuilderStage s){
		back = s;
		unit = u;
		inv = new InventoryMenu(u, INVENTORY_X, INVENTORY_Y){{
			drawCost = true;
			setWidth(135);
		}};
		
		addEntity(inv);
		
		UnitInfo ui = new UnitInfo(5,5);
		ui.setUnit(u);
		addEntity(ui);
		
		shop = new ShopMenu(SHOP_X, SHOP_Y);
		shop.clearSelection();
		
		addEntity(shop);
		
		levelUp = new Button(INVENTORY_X, INVENTORY_Y + 85, "Level Up", Color.green, 135){
			public void onStep(){
				String exp =  Unit.getExpCost(unit.get("Lvl") + 1)+"";
				if(unit.get("Lvl") == 20)
					exp = "--";
				text = "Level Up: " +exp + " EXP";
			}
		};
		levelDown = new Button(INVENTORY_X, INVENTORY_Y + 109, "Level Down", Color.red, 135){
			public void onStep(){
				String exp =  Unit.getExpCost(unit.get("Lvl"))+"";
				if(unit.get("Lvl") == 1)
					exp = "--";
				text = "Level Down: " + exp + " EXP";
			}
		};;
		
		addEntity(levelUp);
		addEntity(levelDown);
		
		state = new Normal();
	}
	
	public void render(){
		super.render();
		Renderer.drawString("default_med", "Items", INVENTORY_X, INVENTORY_Y - 14, 0);
		Renderer.drawString("default_med", "Funds: " + back.getFunds(), INVENTORY_X, INVENTORY_Y + 150, 0);
		Renderer.drawString("default_med", "EXP: " + back.getExp(), INVENTORY_X, INVENTORY_Y + 166, 0);
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
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && repeatTimers[1] == 0) {
			repeatTimers[1] = 0.15f;
			state.down();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && repeatTimers[2] == 0) {
			repeatTimers[2] = 0.15f;
			state.left();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && repeatTimers[3] == 0) {
			repeatTimers[3] = 0.15f;
			state.right();
		}
		for(KeyboardEvent ke : keys) {
			if(ke.state) {
				if(ke.key == Keyboard.KEY_Z) {
					state.select();
				} else if (ke.key == Keyboard.KEY_X){
					state.cancel();
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
	
	private abstract class State{
		public abstract void up();
		public abstract void down();
		public abstract void left();
		public abstract void right();
		public abstract void select();
		public abstract void cancel();
	}
	
	private class Normal extends State {
		@Override
		public void up() {
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
				if(unit.get("Lvl") != 20){
					int cost = Unit.getExpCost(unit.get("Lvl") + 1);
					if(cost <= back.getExp()){
						unit.setLevel(unit.get("Lvl") + 1);
						back.setExp(back.getExp() - cost);
					}
				}
			} else if(levelDown.hovered()){
				if(unit.get("Lvl") != 1){
					int cost = Unit.getExpCost(unit.get("Lvl"));
					unit.setLevel(unit.get("Lvl")-1);
					back.setExp(back.getExp() + cost);
				}
			} else {
				if(inv.getSelection() != null){
					Item i = inv.getSelection().getItem();
					if(!(i instanceof Weapon && ((Weapon) i).pref != null)){
						back.setFunds(back.getFunds() + i.getCost());
						unit.removeFromInventory(i);
					}
				} else {
					inv.clearSelection();
					shop.restoreSelection();
					state = new Shop();
				}
			}
		}

		@Override
		public void cancel() {
			FEMultiplayer.setCurrentStage(back);
		}
		
	}
	
	private class Shop extends State{
		@Override
		public void up() {
			shop.up();
		}

		@Override
		public void down() {
			shop.down();
		}

		@Override
		public void left() {
			shop.left();
		}

		@Override
		public void right() {
			shop.right();
		}

		@Override
		public void select() {
			Item i = shop.getItem();
			back.setFunds(back.getFunds() - i.getCost());
			unit.addToInventory(i);
			cancel();
		}

		@Override
		public void cancel() {
			inv.restoreSelection();
			shop.clearSelection();
			state = new Normal();
		}
		
	}
	

}




