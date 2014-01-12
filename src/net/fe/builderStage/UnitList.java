package net.fe.builderStage;

import java.util.*;

import org.newdawn.slick.Color;

import net.fe.fightStage.FightStage;
import net.fe.unit.*;
import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class UnitList extends Entity {
	private ArrayList<UnitSet> units;
	private List<Unit> selectedUnits;
	private HashMap<Unit, UnitSet> lookup;
	
	//CONFIG
	public static final int WIDTH = 80;
	public static final int HEIGHT = 24;
	public static final int UNITS_PER_ROW = 5;
	
	public UnitList(float x, float y) {
		super(x, y);
		units = new ArrayList<UnitSet>();
		selectedUnits = new ArrayList<Unit>();
		lookup = new HashMap<Unit, UnitSet>();
	}
	
	public void onStep(){
		for(UnitSet u: units){
			u.icon.onStep();
		}
	}
	
	public void render(){
		Renderer.drawRectangle(x-2, y-2, 
				x+WIDTH * UNITS_PER_ROW+2, 
				y + HEIGHT* ((size()-1)/UNITS_PER_ROW+1)+2, 
				1, FightStage.BORDER_DARK);
		Renderer.drawRectangle(x-1, y-1, 
				x+WIDTH * UNITS_PER_ROW +1, 
				y + HEIGHT* ((size()-1)/UNITS_PER_ROW+1)+1, 
				1, FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x, y, 
				x+WIDTH * UNITS_PER_ROW, 
				y + HEIGHT* ((size()-1)/UNITS_PER_ROW+1), 
				1, FightStage.NEUTRAL);
		for(UnitSet set: units){
			float x = set.icon.x;
			float y = set.icon.y;
			set.icon.render();
			if(!selectedUnits.contains(set.unit))
				Renderer.setColor(new Color(0.5f,0.5f,0.5f));
			Renderer.drawString("default_med", set.unit.name, x + 18, y + 2, 0);
			Renderer.setColor(null);
		}
	}
	
	public void refresh(){
		Collections.sort(units, new Comparator<UnitSet>(){
			@Override
			public int compare(UnitSet u1, UnitSet u2) {
				int u1s = selectedUnits.contains(u1.unit)? -1: 0;
				int u2s = selectedUnits.contains(u2.unit)? -1: 0;
				return u1s - u2s;
			}
		});
		for(int i = 0; i < units.size(); i++){
			units.get(i).icon.x = x + (i% UNITS_PER_ROW) * WIDTH + 4;
			units.get(i).icon.y = y + (i/ UNITS_PER_ROW) * HEIGHT + 4;
		}
	}
	
	public void addUnit(Unit u){
		UnitSet s = new UnitSet(u);
		units.add(s);
		lookup.put(u, s);
	}
	
	public void addUnits(Collection<Unit> units){
		for(Unit u: units){
			addUnit(u);
		}
	}
	
	public List<Unit> getSelectedUnits(){
		return new ArrayList<Unit>(selectedUnits);
	}
	
	public List<Unit> getUnits(){
		List<Unit> ans = new ArrayList<Unit>();
		for(UnitSet u: units){
			ans.add(u.unit);
		}
		return ans;
	}
	
	public void selectUnit(Unit u){
		selectedUnits.add(u);
		lookup.get(u).icon.setGreyscale(false);
	}
	
	public void deSelectUnit(Unit u){
		selectedUnits.remove(u);
		lookup.get(u).icon.setGreyscale(true);
	}
	
	public Unit unitAt(int index){
		return units.get(index).unit;
	}
	
	public boolean isSelected(int index){
		return selectedUnits.contains(unitAt(index));
	}
	
	public int size(){
		return units.size();
	}
	
	public int numberSelected(){
		return selectedUnits.size();
	}

}

class UnitSet{
	public Unit unit;
	public UnitIcon icon;
	
	public UnitSet(Unit u){
		unit = u;
		icon = new UnitIcon(u, 0, 0, 0);
		icon.setGreyscale(true);
	}
	
	public String toString(){
		return unit.name;
	}
}
