package net.fe.overworldStage;

import net.fe.unit.Unit;
import chu.engine.Stage;

public class OverworldStage extends Stage{
	private Grid grid;
	
	public OverworldStage(Grid g) {
		super();	
		grid = g;
	}
	
	public Terrain getTerrain(int x, int y){
		return grid.getTerrain(x, y);
	}
	
	public Unit getUnit(int x, int y){
		return grid.getUnit(x, y);
	}
	
	public boolean addUnit(Unit u, int x, int y) {
		if(grid.addUnit(u, x, y)){
			this.addEntity(u);
			return true;
		} else {
			return false;
		}
	}
	
	public Unit removeUnit(int x, int y) {
		Unit u = grid.removeUnit(x, y);
		if(u!=null){
			this.removeEntity(u);
		}
		return u;
	}

	@Override
	public void beginStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub
		
	}
	
}
