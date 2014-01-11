package net.fe.builderStage;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import net.fe.FEResources;
import net.fe.overworldStage.*;
import net.fe.unit.*;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;
import static net.fe.unit.Weapon.Type.*;

public class ShopMenu extends Entity {
	private ItemMenu[] shops;
	private Texture[] shopIcons;
	private int selected;
	private int cameraX;
	private boolean instant;
	
	public static final int HEIGHT = 300;
	public static final int WIDTH = 135;
	public ShopMenu(float x, float y) {
		super(x, y);
		shops = new ItemMenu[9];
		shopIcons = new Texture[9];
		for(int i = 0; i < shops.length; i++){
			shops[i] = new ItemMenu(x + 140*i,y){{
				drawCost = true;
				setWidth(135);
			}};
			shopIcons[i] = FEResources.getTexture("shop" + i);
		}
		for(Weapon w: WeaponFactory.getAllWeapons()){
			ItemDisplay i = new ItemDisplay(0, 0, w.getCopy(), false);
			if(w.pref != null || w.name.startsWith("Debug") || w.getCost() == 1) continue;
			switch(w.type){
			case SWORD: shops[0].addItem(i); break;
			case LANCE: shops[1].addItem(i); break;
			case AXE: shops[2].addItem(i); break;
			case BOW: shops[3].addItem(i); break;
			case LIGHT: shops[4].addItem(i); break;
			case ANIMA: shops[5].addItem(i); break;
			case DARK: shops[6].addItem(i); break;
			case STAFF: shops[7].addItem(i); break;
			}
		}
		shops[8].addItem(new ItemDisplay(0,0,HealingItem.VULNERARY.getCopy(), false));
		shops[8].addItem(new ItemDisplay(0,0,HealingItem.CONCOCTION.getCopy(), false));
		shops[8].addItem(new ItemDisplay(0,0,HealingItem.ELIXIR.getCopy(), false));
		
		for(ItemMenu shop: shops){
			shop.sortItems();
		}
	}
	
	public void onStep(){
		int shouldX = selected*140;
		float dx = Math.signum(shouldX - cameraX) * Game.getDeltaSeconds() * 600;
		cameraX+= dx;
		if(dx * (shouldX - cameraX) < 0 || instant){
			cameraX = shouldX;
			instant = false;
		}
	}
	
	public void render(){
		Renderer.addClip(x, y-16, getSelectedShop().getWidth(), HEIGHT+16, true);
		for(int i = 0; i < shops.length; i++){
			shops[i].x = x + 140*i - cameraX;
			if(shops[i].x >= x - 140 && shops[i].x <= x + 140)
				shops[i].render();
			
			Color c = new Color(0.5f,0.5f,0.5f);
			if(i == selected) c = new Color(Color.white);
			float iconX = x + i*15;
			Renderer.setColor(c);
			Renderer.render(shopIcons[i], 0, 0, 1, 1, iconX, y-15, iconX + 15, y, renderDepth);
			Renderer.setColor(Color.white);
		}
		Renderer.removeClip();
		
	}
	
	private ItemMenu getSelectedShop(){
		return shops[selected];
	}
	
	public Item getItem(){
		return getSelectedShop().getSelection().getItem().getCopy();
	}
	
	public void left(){
		selected--;
		if(selected< 0){
			selected+= shops.length;
			instant = true;
		}
		getSelectedShop().setSelection(0);
	}
	
	public void right(){
		selected++;
		if(selected >= shops.length){
			selected -= shops.length;
			instant = true;
		}
		getSelectedShop().setSelection(0);
	}
	
	public void up(){
		getSelectedShop().up();
	}
	
	public void down(){
		getSelectedShop().down();
	}
	
	public void clearSelection(){
		for(ItemMenu shop: shops){
			shop.clearSelection();
		}
	}
	
	public void restoreSelection(){
		for(ItemMenu shop: shops){
			shop.restoreSelection();
		}
	}

}
