package net.fe.builderStage;

import java.util.*;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import net.fe.unit.*;
import net.fe.*;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class TeamSelectionStage extends Stage {
	private UnitList vassalList;
	private UnitList lordList;
	private TeamBuilderStage builderStage;
	private Cursor cursor;
	private Button ok;
	
	private int maxUnits = 8;
	private float[] repeatTimers = new float[4];
	
	//CONFIG
	public static final int 
	UNIT_LIST_X = 42, UNIT_LIST_Y  = 100, LORD_LIST_X = 42, LORD_LIST_Y = 40,
	OK_BUTTON_X = 330, OK_BUTTON_Y = 280;
	
	
	public TeamSelectionStage(TeamBuilderStage stage){
		builderStage = stage;
		cursor = new Cursor();
		ok = new Button(OK_BUTTON_X, OK_BUTTON_Y, "OK", Color.green, 95);
		addEntity(cursor);
		addEntity(ok);

		List<Unit> vassals = UnitFactory.getVassals();
		List<Unit> lords = UnitFactory.getLords();
		
		lordList = new UnitList(LORD_LIST_X, LORD_LIST_Y);
		lordList.addUnits(lords);
		addEntity(lordList);
		
		vassalList = new UnitList(UNIT_LIST_X, UNIT_LIST_Y);
		vassalList.addUnits(vassals);
		addEntity(vassalList);
		
		Collections.shuffle(vassals);
		Collections.shuffle(lords);
		
		for(int i = 0; i < 8; i++){
			selectUnit(vassals.get(i));
		}
		selectUnit(lords.get(0));
		
		refresh();
	}
	
	public void selectUnit(Unit u){
		if(u.getTheClass().name.equals("Lord")){
			lordList.selectUnit(u);
		} else {
			vassalList.selectUnit(u);
		}
	}
	
	public List<Unit> getSelectedUnits(){
		ArrayList<Unit> units = new ArrayList<Unit>();
		units.addAll(lordList.getSelectedUnits());
		units.addAll(vassalList.getSelectedUnits());
		
		return units;
	}
	
	public void refresh(){
		lordList.refresh();
		vassalList.refresh();
	}
	@Override
	public void beginStep() {
		for(Entity e: entities){
			e.beginStep();
		}
		MapAnimation.updateAll();
		List<KeyboardEvent> keys = Game.getKeys();
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && repeatTimers[0] == 0) {
			repeatTimers[0] = 0.15f;
			up();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && repeatTimers[1] == 0) {
			repeatTimers[1] = 0.15f;
			down();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && repeatTimers[2] == 0) {
			repeatTimers[2] = 0.15f;
			left();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && repeatTimers[3] == 0) {
			repeatTimers[3] = 0.15f;
			right();
		}
		for(KeyboardEvent ke : keys) {
			if(ke.state) {
				if(ke.key == Keyboard.KEY_Z) {
					if(cursor.on){
						cursor.select();
					} else {
						builderStage.setUnits(getSelectedUnits());
						FEMultiplayer.setCurrentStage(builderStage);
					}
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
	
	private void up(){
		if(cursor.on){
			boolean below = cursor.index >= lordList.size();
			cursor.index -= UnitList.UNITS_PER_ROW;
			if(cursor.index < lordList.size() && below){
				cursor.index = lordList.size() - 1;
			}
			checkFlow();
		} else {
			cursor.index = lordList.size() + vassalList.size() - 1;
			cursor.on = true;
			ok.setHover(false);
		}
	}
	
	private void down(){
		if(cursor.on){
			boolean above = cursor.index < lordList.size();
			cursor.index += UnitList.UNITS_PER_ROW;
			if(cursor.index >= lordList.size() && above){
				cursor.index = lordList.size();
			}
			checkFlow();
		} else {
			cursor.index = 0;
			cursor.on = true;
			ok.setHover(false);
		}
	}
	
	private void left(){
		if(cursor.on){
			cursor.index --;
			checkFlow();
		} else {
			cursor.index = lordList.size() + vassalList.size() - 1;
			cursor.on = true;
			ok.setHover(false);
		}
	}
	
	private void right(){
		if(cursor.on){
			cursor.index ++;
			checkFlow();
		} else {
			cursor.index = 0;
			cursor.on = true;
			ok.setHover(false);
		}
	}
	
	private void checkFlow(){
		if(cursor.index < 0 || cursor.index >= lordList.size() + vassalList.size()){
			cursor.on = false;
			ok.setHover(true);
		}
	}

	@Override
	public void onStep() {
		
		for(Entity e: entities){
			e.onStep();
		}
	}

	@Override
	public void endStep() {
		for(Entity e: entities){
			e.endStep();
		}
	}
	
	private class Cursor extends Entity{
		int index;
		boolean on = true;
		public Cursor() {
			super(0,0);
			renderDepth = 0.5f;
		}
		
		public void onStep(){
			if(index < lordList.size()){
				x = LORD_LIST_X + (index% UnitList.UNITS_PER_ROW) * UnitList.WIDTH;
				y = LORD_LIST_Y + (index/ UnitList.UNITS_PER_ROW) * UnitList.HEIGHT;
			} else {
				int index = this.index - lordList.size();
				x = UNIT_LIST_X + (index% UnitList.UNITS_PER_ROW) * UnitList.WIDTH;
				y = UNIT_LIST_Y + (index/ UnitList.UNITS_PER_ROW) * UnitList.HEIGHT;
			}
		}
		
		public void select(){
			if(on){
				if(index < lordList.size()){
					if(lordList.isSelected(index)){
						lordList.deSelectUnit(lordList.unitAt(index));
					}
					else if(lordList.numberSelected() == 0){
						lordList.selectUnit(lordList.unitAt(index));
					}
				}
				if(index >= lordList.size()){
					if(vassalList.isSelected(index - lordList.size())){
						vassalList.deSelectUnit(vassalList.unitAt(index - lordList.size()));
					} else if (vassalList.numberSelected() < maxUnits - 1){
						vassalList.selectUnit(vassalList.unitAt(index - lordList.size()));
					}
				}
			}
		}
		
		public void render(){
			if(on)
			Renderer.drawRectangle(x+1, y+1, x+UnitList.WIDTH-1, 
					y + UnitList.HEIGHT-1, renderDepth, new Color(0.7f,0.7f,1,0.4f));
		}
		
	}
}
