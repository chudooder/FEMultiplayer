package net.fe.builderStage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import net.fe.FEResources;
import net.fe.fightStage.FightStage;
import net.fe.unit.Unit;
import net.fe.unit.UnitIcon;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.Sprite;

public class UnitList extends Entity {
	private ArrayList<UnitSet> units;
	private List<Unit> selectedUnits;
	private HashMap<Unit, UnitSet> lookup;
	private int height;
	private int scrollPos;
	private float scrollOffset;
	private Sprite banned, drafted;
	
	//CONFIG
	public static final int WIDTH = 80;
	public static final int HEIGHT = 24;
	public final int unitsPerRow;
	
	public UnitList(float x, float y, int height, int width) {
		super(x, y);
		this.height = height;
		this.unitsPerRow = width;
		units = new ArrayList<UnitSet>();
		selectedUnits = new ArrayList<Unit>();
		lookup = new HashMap<Unit, UnitSet>();
		renderDepth = 0.6f;
		banned = new Sprite();
		banned.addAnimation("DEFAULT", FEResources.getTexture("banned"));
		drafted = new Sprite();
		drafted.addAnimation("DEFAULT", FEResources.getTexture("drafted"));
	}
	
	public void onStep(){
		int scrollOffsetS = scrollPos * HEIGHT;
		float dy = scrollOffsetS - scrollOffset;
		if(Math.abs(dy) > HEIGHT){
			scrollOffset = scrollOffsetS;
		} else {
			scrollOffset += Math.signum(dy) * Game.getDeltaSeconds() * 300;
			if((scrollOffsetS - scrollOffset) * dy < 0){
				scrollOffset = scrollOffsetS;
			}
		}
		for(UnitSet u: units){
			u.icon.onStep();
		}
	}
	
	public void render(){
		Renderer.drawRectangle(x-2, y-2, 
				x+WIDTH * unitsPerRow+2, 
				y + HEIGHT* height +2, 
				0.9f, FightStage.BORDER_DARK);
		Renderer.drawRectangle(x-1, y-1, 
				x+WIDTH * unitsPerRow +1, 
				y + HEIGHT* height +1, 
				0.9f, FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x, y, 
				x+WIDTH * unitsPerRow, 
				y + HEIGHT* height, 
				0.9f, FightStage.NEUTRAL);
		
		Renderer.addClip(x, y-4, WIDTH*unitsPerRow, height*HEIGHT, true);
		for(int i = scrollPos * unitsPerRow; i < (scrollPos + height) * unitsPerRow && i < units.size(); i++){
			UnitSet set = units.get(i);
			set.icon.y = y + set.row * HEIGHT + 4 - scrollOffset;
			float x = set.icon.x;
			float y = set.icon.y;
			set.icon.render();
			if(set.banned) { 
				banned.render(x-1, y-3, 0.0f);
			} else if(set.drafted) { 
				drafted.render(x-1, y-3, 0.0f);
			}
			if(!selectedUnits.contains(set.unit))
				Renderer.setColor(new Color(0.5f,0.5f,0.5f));
			Renderer.drawString("default_med", set.unit.name, x + 18, y + 2, renderDepth);
			Renderer.setColor(null);
		}
		Renderer.removeClip();
		
		float percent = height/((size()-1)/unitsPerRow+1.0f);
		float scrollbarH = percent * (height * HEIGHT-2) - 1;
		float scrollbarPos = (y + height * HEIGHT/((size()-1)/unitsPerRow+1.0f) * scrollOffset/HEIGHT+1);
		float scrollbarX = (x + unitsPerRow * WIDTH - 3);
		
		if(percent != 1){
			Renderer.drawRectangle(scrollbarX, scrollbarPos, scrollbarX+2, scrollbarPos+scrollbarH, 0, Color.gray);
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
			units.get(i).row = i/ unitsPerRow;
			units.get(i).icon.x = x + (i% unitsPerRow) * WIDTH + 4;
			units.get(i).icon.y = y + (i/ unitsPerRow) * HEIGHT + 4;
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
		if(lookup.get(u).banned || lookup.get(u).drafted) 
			return;
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
	
	public Unit getUnit(String name){
		for(UnitSet set: units){
			if(set.unit.name.equals(name)){
				return set.unit;
			}
		}
		return null;
	}
	
	public void ban(String name) {
		for(UnitSet set: units){
			if(set.unit.name.equals(name)){
				set.banned = true;
			}
		}
	}
	
	public void draft(String name) {
		for(UnitSet set: units){
			if(set.unit.name.equals(name)){
				set.drafted = true;
			}
		}
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
	
	public void scrollTo(int index){
		int row = index / unitsPerRow;
		if(row < scrollPos){
			scrollPos = row;
		}
		if(row >= scrollPos + height){
			scrollPos = row - height +1;
		}
	}
	
	public int getScrollPos(){
		return scrollPos;
	}

	public void sort(Comparator<UnitSet> sort) {
		Collections.sort(units, sort);
	}

}

class UnitSet{
	public Unit unit;
	public UnitIcon icon;
	public int row;
	public boolean banned;
	public boolean drafted;
	
	public UnitSet(Unit u){
		unit = u;
		icon = new UnitIcon(u, 0, 0, 0);
		icon.setGreyscale(true);
		banned = false;
		drafted = false;
	}
	
	public String toString(){
		return unit.name;
	}
}
