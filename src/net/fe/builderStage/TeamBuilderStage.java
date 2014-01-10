package net.fe.builderStage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import net.fe.FEMultiplayer;
import net.fe.unit.MapAnimation;
import net.fe.unit.Unit;
import net.fe.unit.UnitFactory;
import net.fe.unit.UnitIcon;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class TeamBuilderStage extends Stage {
	
	private List<Unit> units;
	private Cursor cursor;
	private float[] repeatTimers;
	private int funds;
	private int exp;
	
	//CONFIG
	private static int name = 20, clazz = 90, lv = 160, hgap = 30; //xvals
	private static int yStart = 20, vgap = 20;
	private static int FUNDS = 48000, EXP = 10500;
	
	public TeamBuilderStage() {
		repeatTimers = new float[4];
		units = UnitFactory.getAllUnits().subList(0, 7);
		units.add(UnitFactory.getUnit("Marth"));
		
		cursor = new Cursor(0, yStart-4, 480, vgap, units.size());
		addEntity(cursor);
		
		setFunds(FUNDS);
		setExp(EXP);
		
		int y = yStart;
		float d = 0.1f;
		for(Unit u: units){
			addEntity(new UnitIcon(u, 0, y-2, d));
			y+=vgap;
			d-=0.001f;
		}
	}
	
	@Override
	public void render() {
		super.render();
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
		for (Entity e : entities) {
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
		MapAnimation.updateAll();
		List<KeyboardEvent> keys = Game.getKeys();
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && repeatTimers[0] == 0) {
			repeatTimers[0] = 0.15f;
			cursor.up();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && repeatTimers[1] == 0) {
			repeatTimers[1] = 0.15f;
			cursor.down();
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
					FEMultiplayer.setCurrentStage(new UnitBuilderStage(units.get(cursor.getIndex()), this));
				} else if (ke.key == Keyboard.KEY_X){
					
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

	public int getFunds() {
		return funds;
	}

	public void setFunds(int funds) {
		this.funds = funds;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}
	
	

}

class Cursor extends Entity{
	private int index;
	private int width;
	private int initialY;
	private int height;
	private int max;
	private boolean instant;
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
		if(instant){
			y = supposedY;
			instant = false;
		} else {
			float dy = supposedY - y;
			y+= Math.signum(dy) * Game.getDeltaSeconds() * 300;
			if((supposedY - y) * dy < 0){
				y = supposedY;
			}
		}
	}
	
	public void render(){
		Renderer.drawRectangle(x, y, x+width, y+height, renderDepth, new Color(128,128,213,128));
	}
	
	public void up(){
		index--;
		if(index<0){
			index+= max;
			instant = true;
		}
	}
	
	public void down(){
		index++;
		if(index >= max){
			index -= max;
			instant = true;
		}
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex(int i){
		index = i;
	}
	
}
