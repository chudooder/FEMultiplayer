package net.fe.builderStage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import net.fe.unit.Unit;
import net.fe.unit.UnitFactory;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class TeamBuilderStage extends Stage {
	
	private ArrayList<Unit> units;
	private Cursor cursor;
	
	//CONFIG
	private int name = 0, clazz = 70, lv = 140, hgap = 30; //xvals
	private int yStart = 20, vgap = 20;
	
	public TeamBuilderStage() {
		units = UnitFactory.getAllUnits();
		
		cursor = new Cursor(0, yStart-4, 480, vgap, units.size());
		addEntity(cursor);
	}
	
	@Override
	public void render() {
		super.render();
		int name = 0, clazz = 70, lv = 140, hgap = 30; //xvals
		int yStart = 20, vgap = 20;
		List<String> stats = Arrays.asList(
			"Lvl", "HP", "Str", "Mag", "Skl", "Spd", "Lck", "Def", "Res", "Mov"
		);
		
		Renderer.drawString("default_med", "Name", name, 0, 0);
		Renderer.drawString("default_med", "Class", clazz, 0, 0);
		int x = lv;
		for(String s: stats){
			Renderer.drawString("default_med", s, x, 0, 0);
			x+= hgap;
		}
		
		int y = yStart;
		for(Unit u: units){
			Renderer.drawString("default_med", u.name, name, y, 0);
			Renderer.drawString("default_med", u.getTheClass().name, clazz, y, 0);
			x = lv;
			for(String s: stats){
				Renderer.drawString("default_med", u.getBase(s), x, y, 0);
				x+= hgap;
			}
			y+=vgap;
		}
	}

	@Override
	public void beginStep() {
		List<KeyboardEvent> keys = Game.getKeys();
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			cursor.up();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			cursor.down();
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

}

class Cursor extends Entity{
	private int index;
	private int width;
	private int initialY;
	private int height;
	private int max;
	public Cursor(int x, int y, int width, int height, int max) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.initialY = y;
		renderDepth = 0.5f;
		this.max = max;
	}
	
	public void onStep(){
		int supposedY = initialY + index*height;
		float dy = supposedY - y;
		y+= Math.signum(dy) * Game.getDeltaSeconds() * 300;
		if((supposedY - y) * dy < 0){
			y = supposedY;
		}
	}
	
	public void render(){
		Renderer.drawRectangle(x, y, x+width, y+height, renderDepth, new Color(128,128,213,128));
	}
	
	public void up(){
		index--;
		if(index<0) index+= max;
	}
	
	public void down(){
		index++;
		index%= max;
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex(int i){
		index = i;
	}
	
}
