package net.fe.builderStage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.fe.Button;
import net.fe.ControlsDisplay;
import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.Player;
import net.fe.RunesBg;
import net.fe.Session;
import net.fe.modifier.Modifier;
import net.fe.network.Message;
import net.fe.network.message.DraftMessage;
import net.fe.unit.MapAnimation;
import net.fe.unit.Unit;
import net.fe.unit.UnitFactory;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;

public class TeamSelectionStage extends Stage {
	private UnitList vassalList;
	private UnitList lordList;
	private TeamBuilderStage builderStage;
	private Cursor cursor;
	private Button[] buttons;
	private Button ok;
	private Button classSort;
	private Button nameSort;
	private ControlsDisplay controls;
	
	private int maxUnits = 8;
	private float[] repeatTimers = new float[4];
	
	//CONFIG
	public static final int 
	UNIT_LIST_X = 42, UNIT_LIST_Y  = 100, LORD_LIST_X = 42, LORD_LIST_Y = 40,
	OK_BUTTON_X = 349, BUTTON_Y = 260, CS_BUTTON_X = 41, NS_BUTTON_X = 145;
	
	
	public TeamSelectionStage(TeamBuilderStage stage, Session s){
		super("preparations");
		builderStage = stage;
		if(s == null)
			maxUnits = 8;
		else
			maxUnits = s.getMaxUnits();
		cursor = new Cursor();
		controls = new ControlsDisplay();
		controls.addControl("Z", "Select");
		controls.addControl("Enter", "Done");
		addEntity(controls);
		
		addEntity(new RunesBg(new Color(0xd2b48c)));
		List<Unit> vassals = UnitFactory.getVassals();
		List<Unit> lords = UnitFactory.getLords();
		
		lordList = new UnitList(LORD_LIST_X, LORD_LIST_Y, 2, 5);
		lordList.addUnits(lords);
		addEntity(lordList);
		
		vassalList = new UnitList(UNIT_LIST_X, UNIT_LIST_Y, 5, 5);
		vassalList.addUnits(vassals);
		vassalList.sort(new SortByName());
		addEntity(vassalList);
		if(s != null) {
			for(Modifier m : s.getModifiers()) {
				m.modifyUnits(this);
			}
		}
		ok = new Button(OK_BUTTON_X, BUTTON_Y, "OK", Color.green, 95) {
			public void execute() {
				builderStage.setUnits(getSelectedUnits());
				builderStage.refresh();
				FEMultiplayer.setCurrentStage(builderStage);
			}
		};
		classSort = new Button(CS_BUTTON_X, BUTTON_Y, "Sort By Class", Color.blue, 95) {
			public void execute() {
				vassalList.sort(new SortByClass());
				vassalList.refresh();
			}
		};
		nameSort = new Button(NS_BUTTON_X, BUTTON_Y, "Sort By Name", Color.blue, 95) {
			public void execute() {
				vassalList.sort(new SortByName());
				vassalList.refresh();
			}
		};
		
		buttons = new Button[3];
		buttons[0] = ok;
		buttons[1] = nameSort;
		buttons[2] = classSort;
		addEntity(cursor);
		addEntity(ok);
		addEntity(classSort);
		addEntity(nameSort);
		
		Collections.shuffle(vassals);
		Collections.shuffle(lords);
		
		for(int i = 0; i < maxUnits-1; i++){
			selectUnit(vassals.get(i));
		}
		selectUnit(lords.get(0));
		
		refresh();
	}
	
	public Unit getUnit(String name){
		Unit u = lordList.getUnit(name);
		if(u == null) u = vassalList.getUnit(name);
		return u;
	}
	
	public void selectUnit(Unit u){
		if(u.getTheClass().name.equals("Lord")){
			lordList.selectUnit(u);
		} else {
			vassalList.selectUnit(u);
		}
	}
	
	public void deselectAll(){
		for(Unit u: lordList.getSelectedUnits()){
			lordList.deSelectUnit(u);
		}
		for(Unit u: vassalList.getSelectedUnits()){
			vassalList.deSelectUnit(u);
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
		cursor.index = 0;
		cursor.on = true;
		ok.setHover(false);
		checkFlow();
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
					cursor.select();
				} 
				if(ke.key == Keyboard.KEY_RETURN){
					AudioPlayer.playAudio("select", 1, 1);
					buttons[0].execute();
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
		if(cursor.index < 0)
			buttons[-cursor.index-1].setHover(false);
		AudioPlayer.playAudio("cursor", 1, 1);
		if(cursor.on){
			boolean below = cursor.index >= lordList.size();
			cursor.index -= lordList.unitsPerRow;
			if(cursor.index < -1) cursor.index = -1;
			if(cursor.index < lordList.size() && below){
				cursor.index = lordList.size() - 1;
			}
			
		} else {
			cursor.index = lordList.size() + vassalList.size() - 1;
			cursor.on = true;
			cursor.instant = true;
			ok.setHover(false);
		}
		checkFlow();
	}
	
	private void down(){
		if(cursor.index < 0)
			buttons[-cursor.index-1].setHover(false);
		AudioPlayer.playAudio("cursor", 1, 1);
		if(cursor.on){
			boolean above = cursor.index < lordList.size();
			cursor.index += lordList.unitsPerRow;
			if(cursor.index >= lordList.size() && above){
				cursor.index = lordList.size();
			}
			
		} else {
			cursor.index = 0;
			cursor.instant = true;
			cursor.on = true;
		}
		checkFlow();
	}
	
	private void left(){
		AudioPlayer.playAudio("cursor", 1, 1);
		if(cursor.index < 0)
			buttons[-cursor.index-1].setHover(false);
		cursor.index --;
		checkFlow();
	}
	
	private void right(){
		AudioPlayer.playAudio("cursor", 1, 1);
		if(cursor.index < 0)
			buttons[-cursor.index-1].setHover(false);
		cursor.index ++;
		checkFlow();
		if(cursor.index == 0){
			cursor.instant = true;
		}
	}
	
	private void checkFlow(){
		if(cursor.index >= lordList.size() + vassalList.size()) {
			cursor.index = -buttons.length;
		}
		if(-cursor.index > buttons.length) {
			cursor.on = true;
			cursor.instant = true;
			cursor.index = lordList.size() + vassalList.size() - 1;
			vassalList.scrollTo(vassalList.size() - 1);
		}
		if(cursor.index < 0){
			cursor.on = false;
			buttons[-cursor.index-1].setHover(true);
		} else {
			cursor.on = true;
			if(cursor.index >= lordList.size()){
				vassalList.scrollTo(cursor.index - lordList.size());
			}
		}
	}
	
	public void render(){
		super.render();
		
		Renderer.drawRectangle(0, 0, 480, 20, 0, new Color(0,0,0,0.5f));
		
		int lords = 1- lordList.numberSelected();
		int vassals = maxUnits - 1 - vassalList.numberSelected();
		String ls = lords + " more lord" + (lords == 1? "": "s");
		String vs = vassals + " more vassal" + (vassals == 1? "": "s");
		String s = "You may select " + ls + " and " + vs;
		int width = FEResources.getBitmapFont("default_med").getStringWidth(s);
		Renderer.drawString("default_med", s, 240 - width/2, 5, 0);
		
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
	
	public int getMaxUnits(){
		return maxUnits;
	}
	
	public List<Unit> getAllUnits() {
		List<Unit> ans = new ArrayList<Unit>();
		ans.addAll(vassalList.getUnits());
		ans.addAll(lordList.getUnits());
		return ans;
	}
	
	private class Cursor extends Entity{
		int index;
		boolean on = true;
		boolean instant = false;
		public Cursor() {
			super(0,0);
			renderDepth = 0.5f;
		}
		
		public void onStep(){
			if(!on){
				return;
			}
			int supposedX, supposedY;
			if(index < lordList.size()){
				supposedX = LORD_LIST_X + (index% lordList.unitsPerRow) * UnitList.WIDTH;
				supposedY = LORD_LIST_Y + (index/ lordList.unitsPerRow) * UnitList.HEIGHT - lordList.getScrollPos() * UnitList.HEIGHT;
			} else {
				int index = this.index - lordList.size();
				supposedX = UNIT_LIST_X + (index% lordList.unitsPerRow) * UnitList.WIDTH;
				supposedY = UNIT_LIST_Y + (index/ lordList.unitsPerRow) * UnitList.HEIGHT - vassalList.getScrollPos() * UnitList.HEIGHT;
			}
			if(Math.abs(supposedX - x) > UnitList.WIDTH || 
					Math.abs(supposedY-y) > UnitList.HEIGHT || instant){
				instant = false;
				y = supposedY;
				x = supposedX;
			} else {
				float dy = supposedY - y;
				y+= Math.signum(dy) * Game.getDeltaSeconds() * 300;
				if((supposedY - y) * dy < 0){
					y = supposedY;
				}
				float dX = supposedX - x;
				x+= Math.signum(dX) * Game.getDeltaSeconds() * 1200;
				if((supposedX - x) * dX < 0){
					x = supposedX;
				}
			}
		}
		
		public void select(){
			
			if(on){
				if(index < lordList.size()){
					if(lordList.isSelected(index)){
						AudioPlayer.playAudio("select", 1, 1);
						lordList.deSelectUnit(lordList.unitAt(index));
					}
					else if(lordList.numberSelected() == 0){
						AudioPlayer.playAudio("select", 1, 1);
						lordList.selectUnit(lordList.unitAt(index));
					}
				}
				if(index >= lordList.size()){
					if(vassalList.isSelected(index - lordList.size())){
						AudioPlayer.playAudio("select", 1, 1);
						vassalList.deSelectUnit(vassalList.unitAt(index - lordList.size()));
					} else if (vassalList.numberSelected() < maxUnits - 1){
						AudioPlayer.playAudio("select", 1, 1);
						vassalList.selectUnit(vassalList.unitAt(index - lordList.size()));
					}
				}
			} else {
				AudioPlayer.playAudio("select", 1, 1);
				buttons[-cursor.index-1].execute();
			}
		}
		
		public void render(){
			if(on)
			Renderer.drawRectangle(x+1, y+1, x+UnitList.WIDTH-1, 
					y + UnitList.HEIGHT-1, renderDepth, new Color(0.7f,0.7f,1,0.4f));
		}
		
	}
	
	private class SortByClass implements Comparator<UnitSet> {
		@Override
		public int compare(UnitSet arg0, UnitSet arg1) {
			return arg0.unit.getTheClass().name.compareTo(arg1.unit.getTheClass().name);
		}
	}
	
	private class SortByName implements Comparator<UnitSet> {
		@Override
		public int compare(UnitSet arg0, UnitSet arg1) {
			return arg0.unit.name.compareTo(arg1.unit.name);
		}
	}
}
