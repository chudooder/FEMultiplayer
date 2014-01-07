package net.fe.builderStage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.fe.unit.Unit;
import net.fe.unit.UnitFactory;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class TeamBuilderStage extends Stage {
	
	private ArrayList<Unit> units;
	
	public TeamBuilderStage() {
		units = UnitFactory.getAllUnits();
	}
	
	@Override
	public void render() {
		int name = 0, clazz = 70, lv = 120, hp = 135;
		List<String> stats = Arrays.asList(
			"Lvl", "HP", "Str", "Mag", "Spd", "Skl"
		);
		
		Renderer.drawString("default_med", "Name", name, 0, 0);
		Renderer.drawString("default_med", "Class", clazz, 0, 0);
		Renderer.drawString("default_med", "Lv", lv, 0, 0);
		Renderer.drawString("default_med", "HP", hp, 0, 0);
//		Renderer.drawString("default_med", "Name", name, 0, 0);
//		Renderer.drawString("default_med", "Name", name, 0, 0);
//		for(int i=0; i<units.size(); i++) {
//			Unit unit = units.get(i);
//			Renderer.drawString("default_med", unit.name, 0, 16*i, 0.0f);
//			Renderer.drawString("default_med", unit.getTheClass().name, 100, 16*i, 0.0f);
//		}
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
