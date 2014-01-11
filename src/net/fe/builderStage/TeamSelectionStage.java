package net.fe.builderStage;

import java.util.*;

import net.fe.unit.MapAnimation;
import net.fe.unit.Unit;
import net.fe.unit.UnitFactory;
import chu.engine.Entity;
import chu.engine.Stage;

public class TeamSelectionStage extends Stage {
	private UnitList vassalList;
	private UnitList lordList;
	private TeamBuilderStage builderStage;
	
	//CONFIG
	public static final int 
	UNIT_LIST_X = 50, UNIT_LIST_Y  = 120, LORD_LIST_X = 50, LORD_LIST_Y = 50; 
	
	
	public TeamSelectionStage(TeamBuilderStage stage){
		builderStage = stage;

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
	}

	@Override
	public void onStep() {
		MapAnimation.updateAll();
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

}
