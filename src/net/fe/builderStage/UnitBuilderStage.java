package net.fe.builderStage;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.overworldStage.InventoryMenu;
import net.fe.overworldStage.ItemMenu;
import net.fe.unit.ItemDisplay;
import net.fe.unit.MapAnimation;
import net.fe.unit.Unit;
import net.fe.unit.WeaponFactory;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class UnitBuilderStage extends Stage {
	private Unit unit;
	private ItemMenu shop;
	private InventoryMenu inv;
	private float[] repeatTimers = new float[4];
	private TeamBuilderStage back;
	private State state;
	private Button levelUp, levelDown;
	
	//CONFIG
	public static final int
	INVENTORY_X = 50, INVENTORY_Y = 100, SHOP_X = 220, SHOP_Y = 100;
	
	public UnitBuilderStage(Unit u, TeamBuilderStage s){
		back = s;
		unit = u;
		inv = new InventoryMenu(u, INVENTORY_X, INVENTORY_Y){{
			drawCost = true;
			width = 135;
		}};
		
		addEntity(inv);
		
		shop = new ItemMenu(SHOP_X, SHOP_Y){{
			drawCost = true;
			width = 135;
		}};
		shop.addItem(new ItemDisplay(0,0,WeaponFactory.getWeapon("Iron Sword"), false));
		shop.addItem(new ItemDisplay(0,0,WeaponFactory.getWeapon("Steel Sword"), false));
		shop.clearSelection();
		
		addEntity(shop);
		
		levelUp = new Button(INVENTORY_X, INVENTORY_Y + 100, "Level Up", Color.green){
			public void onStep(){
				text = "Level Up: " +Unit.getExpCost(unit.get("Lvl") + 1) + " EXP";
			}
		};
		levelDown = new Button(INVENTORY_X, INVENTORY_Y + 124, "Level Down", Color.red){
			public void onStep(){
				text = "Level Down: " +Unit.getExpCost(unit.get("Lvl")) + " EXP";
			}
		};;
		
		addEntity(levelUp);
		addEntity(levelDown);
		
		state = new Normal();
	}
	
	public void render(){
		super.render();
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
			// TODO Auto-generated method stub
		}

		@Override
		public void cancel() {
			FEMultiplayer.setCurrentStage(back);
		}
		
	}
	
	private class Button extends Entity{
		protected String text;
		private Color color;
		private boolean hover;
		public Button(float x, float y, String text, Color color) {
			super(x, y);
			this.text = text;
			this.color = color;
		}
		

		
		public void render(){
			int width = FEResources.getBitmapFont("default_med").getStringWidth(text);
			Color c = new Color(color);
			if(!hover)
				c = c.darker();
			Renderer.drawRectangle(x, y, x+135, y+20, renderDepth, c);
			Renderer.drawString("default_med", text, x+67-width/2 + 2, y + 4, renderDepth);
			
		}
		
		public void setHover(boolean hover){
			this.hover = hover;
		}
		
		public boolean hovered(){
			return hover;
		}
	}
}




