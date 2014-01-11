package net.fe.builderStage;

import java.util.*;

import net.fe.unit.*;
import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class UnitList extends Entity {
	private ArrayList<UnitSet> units;
	private List<Unit> selectedUnits;
	private HashMap<String, UnitSet> lookup;
	
	//CONFIG
	public static final int WIDTH = 80;
	public static final int HEIGHT = 20;
	public static final int UNITS_PER_ROW = 5;
	
	public UnitList(float x, float y) {
		super(x, y);
		units = new ArrayList<UnitSet>();
		selectedUnits = new ArrayList<Unit>();
		lookup = new HashMap<String, UnitSet>();
	}
	
	public void onStep(){
		for(UnitSet u: units){
			u.icon.onStep();
		}
	}
	
	public void render(){
		for(UnitSet set: units){
			float x = set.icon.x;
			float y = set.icon.y;
			set.icon.render();
			Renderer.drawString("default_med", set.unit.name, x + 16, y + 1, 0);
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
			units.get(i).icon.x = x + (i% UNITS_PER_ROW) * WIDTH + 2;
			units.get(i).icon.y = y + (i/ UNITS_PER_ROW) * HEIGHT + 2;
		}
	}
	
	public void addUnit(Unit u){
		UnitSet s = new UnitSet(u);
		units.add(s);
		lookup.put(u.name, s);
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
		u = lookup.get(u.name).unit;
		selectedUnits.add(u);
		lookup.get(u.name).icon.setGreyscale(false);
	}
	
	public void deSelectUnit(Unit u){
		u = lookup.get(u.name).unit;
		selectedUnits.remove(u);
		lookup.get(u.name).icon.setGreyscale(true);
	}
	
	public Unit unitAt(int index){
		return units.get(index).unit;
	}
	
	public int size(){
		return units.size();
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
