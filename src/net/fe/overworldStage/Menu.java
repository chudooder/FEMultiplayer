package net.fe.overworldStage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class Menu extends Entity {
	private List<String> items;
	private int selection;
	
	public static final Color MENU = new Color(0xAAAAAA);
	public static final Color MENU_SELECT = new Color(0x777777);
	public Menu(float x, float y, String... items) {
		super(x, y);
		this.items = Arrays.asList(items);
		renderDepth = OverworldStage.MENU_DEPTH;
	}
	
	public void addItem(String item){
		items.add(item);
	}
	
	public void removeItem(String item){
		items.remove(item);
	}
	
	public void render(){
		int oY = 0;
		for(int i = 0; i < items.size(); i++){
			Color c = selection == i? MENU_SELECT: MENU;
			Renderer.drawRectangle(x, y + oY, x + 60, y + oY + 16, 0, c);
			Renderer.drawString("default_med", items.get(i), x+1, y + oY + 1, 0);
			oY+=17;
		}
	}
	
}
