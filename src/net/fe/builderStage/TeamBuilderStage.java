package net.fe.builderStage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import net.fe.Button;
import net.fe.FEMultiplayer;
import net.fe.fightStage.FightStage;
import net.fe.unit.Item;
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
	private TeamSelectionStage select;
	private Button fight, save, load;
	private Button[] buttons;
	private int currButton;
	
	//CONFIG
	private static int name = 30, clazz = 100, lv = 170, hgap = 30; //xvals
	private static int yStart = 40, vgap = 20, table_ystart = 10;
	private static int FUNDS = 48000, EXP = 84000;
	
	public static final String EXT = "femt";
	
	public TeamBuilderStage() {
		repeatTimers = new float[4];
		
		select = new TeamSelectionStage(this);
		units = select.getSelectedUnits();
		
		buttons = new Button[3];
		currButton = 2;
		
		fight = new Button(390, 290, "Fight!", Color.green, 80){
			@Override
			public void execute() {
				// TODO Go to the lobby
				
			}
			
		};
		buttons[2] = fight;
		addEntity(fight);
		
		save = new Button(220, 290, "Save", Color.blue, 80){
			@Override
			public void execute() {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter(
						"FE Multiplayer Team", EXT));
				chooser.setCurrentDirectory(new File("."));
				int ret = chooser.showSaveDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION){
					saveTeam(convertPath(chooser.getSelectedFile().toString()));
				}
			}
			
		};
		buttons[0] = save;
		addEntity(save);
		
		load = new Button(305, 290, "Load", Color.blue, 80){
			@Override
			public void execute() {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter(
						"FE Multiplayer Team", EXT));
				chooser.setCurrentDirectory(new File("."));
				int ret = chooser.showOpenDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION){
					loadTeam(convertPath(chooser.getSelectedFile().toString()));
				}
			}
		};
		buttons[1] = load;
		addEntity(load);
		
		
		
		cursor = new Cursor(9, yStart-4, 462, vgap, units.size());
		cursor.on = true;
		addEntity(cursor);
		
		setFunds(FUNDS);
		setExp(EXP);
		
		int y = yStart;
		float d = 0.1f;
		for(Unit u: units){
			addEntity(new UnitIcon(u, 10, y-2, d));
			y+=vgap;
			d-=0.001f;
		}
	}
	
	public static String convertPath(String path){
		if(!path.endsWith(EXT)){
			return path+EXT;
		} else {
			return path;
		}
	}
	
	public void setUnits(List<Unit> units){
		this.units.removeAll(units);
		for(Unit u: this.units){
			funds += u.squeezeGold();
			exp += u.squeezeExp();
		}
		this.units = units;
		for(Entity e: entities){
			if(e instanceof UnitIcon) e.destroy();
		}
		int y = yStart;
		float d = 0.1f;
		for(Unit u: units){
			addEntity(new UnitIcon(u, 10, y-2, d));
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
		
		Renderer.drawBorderedRectangle(9, table_ystart-2, 471, table_ystart+14, 1f, 
				FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
		
		Renderer.drawString("default_med", "Name", name, table_ystart, 0);
		Renderer.drawString("default_med", "Class", clazz, table_ystart, 0);
		int x = lv;
		for(String s: stats){
			Renderer.drawString("default_med", s, x, table_ystart, 0);
			x+= hgap;
		}
		
		Renderer.drawBorderedRectangle(
				9, yStart-6, 471, yStart + vgap * units.size()-2, 1f, 
				FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
		
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
			if(!cursor.on){
				buttons[currButton].setHover(false);
				cursor.on = true;
				cursor.index = cursor.max - 1;
				cursor.instant = true;
			}else if(cursor.index == 0){
				cursor.on = false;
				buttons[currButton].setHover(true);
			} else {
				cursor.up();
			}
			
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && repeatTimers[1] == 0) {
			repeatTimers[1] = 0.15f;
			if(!cursor.on){
				buttons[currButton].setHover(false);
				cursor.on = true;
				cursor.index = 0;
				cursor.instant = true;
			}else if(cursor.index == cursor.max -1){
				cursor.on = false;
				buttons[currButton].setHover(true);
			} else {
				cursor.down();
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && repeatTimers[2] == 0) {
			repeatTimers[2] = 0.15f;
			if(!cursor.on){
				buttons[currButton].setHover(false);
				currButton--;
				if(currButton < 0) currButton+=3;
				buttons[currButton].setHover(true);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && repeatTimers[3] == 0) {
			repeatTimers[3] = 0.15f;
			if(!cursor.on){
				buttons[currButton].setHover(false);
				currButton++;
				currButton%=0;
				buttons[currButton].setHover(true);
			}
		}
		for(KeyboardEvent ke : keys) {
			if(ke.state) {
				if(ke.key == Keyboard.KEY_Z) {
					if(cursor.on)
						FEMultiplayer.setCurrentStage(new UnitBuilderStage(units.get(cursor.getIndex()), this));
					else
						buttons[currButton].execute();
				} else if (ke.key == Keyboard.KEY_X){
					select.refresh();
					FEMultiplayer.setCurrentStage(select);
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

	public void refresh() {
		cursor.destroy();
		cursor = new Cursor(9, yStart-4, 462, vgap, units.size());
		cursor.on = true;
		addEntity(cursor);
	}
	
	public boolean saveTeam(String location){
		String[][] teamData = new String[units.size()][6];
		for(int i = 0; i < units.size(); i++){
			Unit u = units.get(i);
			teamData[i][0] = u.name;
			teamData[i][1] = u.get("Lvl") + "";
			for(int j = 0; j < u.getInventory().size(); j++){
				teamData[i][2+j] = u.getInventory().get(j).name;
			}
		}
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(location));
			out.writeObject(teamData);
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean loadTeam(String location){
		String[][] teamData;
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(location));
			teamData = (String[][]) in.readObject();
			in.close();
		} catch (IOException e){
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		select.deselectAll();
		setUnits(new ArrayList<Unit>());
		for(int i = 0; i < select.getMaxUnits(); i++){
			Unit u = select.getUnit(teamData[i][0]);
			int lv = Integer.parseInt(teamData[i][1]);
			while(u.get("Lvl") != lv){
				int expCost = Unit.getExpCost(u.get("Lvl") + 1);
				if(expCost < exp){
					exp -= expCost;
					u.setLevel(u.get("Lvl")+1);
				} else {
					break;
				}
			}
			for(int j = 2; j < 6; j++){
				String itemName = teamData[i][j];
				if(itemName != null){
					Item item = Item.getItem(itemName);
					int goldCost = item.getCost();
					if(goldCost < funds){
						funds -= goldCost;
						u.addToInventory(item);
					} 
				} else {
					break;
				}
			}
			select.selectUnit(u);
		}
		setUnits(select.getSelectedUnits());
		
		return true;
	}
}

class Cursor extends Entity{
	int index;
	private int width;
	private int initialY;
	private int height;
	int max;
	boolean instant;
	boolean on;
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
		if(on){
		if(max == 0)
			Renderer.drawString("default_med", "Press X to select units", 200, 154, renderDepth);
		else
			Renderer.drawRectangle(x, y, x+width, y+height, renderDepth, new Color(128,128,213,128));
		}
	}
	
	public void up(){
		if(max == 0) return;
		index--;
		if(index<0){
			index+= max;
			instant = true;
		}
	}
	
	public void down(){
		if(max == 0) return;
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
