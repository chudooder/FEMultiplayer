package net.fe.overworldStage;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class Menu<T> extends Entity {
	protected List<T> items;
	protected int selection;
	private int width;
	protected int height;
	private boolean cleared;
	
	private int marked = -1;
	
	public static final Color MENU = new Color(0xBBBBBB);
	public static final Color MENU_SELECT = new Color(0x999999);
	public static final Color MENU_MARKED = new Color(0x777777);
	public Menu(float x, float y) {
		super(x, y);
		this.items = new ArrayList<T>();
		renderDepth = ClientOverworldStage.MENU_DEPTH;
		width = 60;
		height = 16;
	}
	
	public Menu(){
		this(0,0);
	}
	
	public void addItem(T item){
		items.add(item);
	}
	
	public void removeItem(T item){
		items.remove(item);
	}
	
	public void render(){
		int oY = 0;
		for(int i = 0; i < items.size(); i++){
			Color c = (selection == i && !cleared)? MENU_SELECT: MENU;
			if(i == marked){
				c = MENU_MARKED;
			}
			Renderer.drawRectangle(x, y + oY, x + width, y + oY + height, renderDepth, c);
			renderItem(items.get(i), oY);
			oY+=height+1;
		}
	}
	
	public void renderItem(T item, int offsetY){
		Renderer.drawString("default_med", String.valueOf(item), x+1, y + offsetY + 1, 0);
	}
	
	public void down(){
		selection = (selection+1)%items.size();
		if(selection == marked) down();
	}
	
	public void up(){
		selection = (selection-1)%items.size();
		if(selection < 0){
			selection+= items.size();
		}
		if(selection == marked) up();
	}
	
	public int size(){
		return items.size();
	}
	
	public T get(int index) {
		return items.get(index);
	}
	
	public void setSelection(int index){
		selection = index;
		if(selection == marked) down();
	}
	
	public T getSelection(){
		return items.get(selection);
	}
	
	public int getSelectedIndex(){
		return selection;
	}
	
	public T getMarked(){
		if(marked == -1) return null;
		return items.get(marked);
	}
	
	public int getMarkedIndex(){
		return marked;
	}
	
	public void clearSelection(){
		cleared = true;
	}
	
	public void restoreSelection(){
		cleared = false;
	}
	
	public boolean hasSelection(){
		return !cleared;
	}
	
	public void mark(int i){
		marked = i;
	}
	
	public void unmark(){
		marked = -1;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
}
