package net.fe.builderStage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.fe.unit.Unit;
import net.fe.unit.UnitFactory;
import chu.engine.Entity;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class TeamBuilderStage extends Stage {
	
	private ArrayList<Unit> units;
	
	public TeamBuilderStage() {
		units = UnitFactory.getAllUnits();
	}
	
	@Override
	public void render() {
		int name = 0, clazz = 70, lv = 140, hgap = 30; //xvals
		int yStart = 20, vgap = 20;
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

class Cursor extends Entity{
	int index;
	public Cursor(float x, float y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
}
