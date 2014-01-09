package net.fe.builderStage;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.fe.FEMultiplayer;
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

public class UnitBuilderStage extends Stage {
	private Unit unit;
	private ItemMenu shop;
	private float[] repeatTimers = new float[4];
	private TeamBuilderStage back;
	private boolean flag;
	
	//CONFIG
	public static final int
	INVENTORY_X = 50, INVENTORY_Y = 100, SHOP_X = 220, SHOP_Y = 100;
	
	public UnitBuilderStage(Unit u, TeamBuilderStage s){
		back = s;
		unit = u;
		addEntity(new InventoryMenu(u, INVENTORY_X, INVENTORY_Y){{
			drawCost = true;
			width = 135;
		}});
		shop = new ItemMenu(SHOP_X, SHOP_Y){{
			drawCost = true;
			width = 135;
		}};
		shop.addItem(new ItemDisplay(0,0,WeaponFactory.getWeapon("Iron Sword"), false));
		shop.addItem(new ItemDisplay(0,0,WeaponFactory.getWeapon("Steel Sword"), false));
		shop.clearSelection();
		
		addEntity(shop);
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
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && repeatTimers[1] == 0) {
			repeatTimers[1] = 0.15f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && repeatTimers[2] == 0) {
			repeatTimers[2] = 0.15f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && repeatTimers[3] == 0) {
			repeatTimers[3] = 0.15f;
		}
		for(KeyboardEvent ke : keys) {
			if(ke.state) {
				if(ke.key == Keyboard.KEY_Z) {
					
				} else if (ke.key == Keyboard.KEY_X){
					FEMultiplayer.setCurrentStage(back);
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
	
	private class Cursor extends Entity{
		public Cursor(float x, float y) {
			super(x, y);
			// TODO Auto-generated constructor stub
		}
		
	}
}


