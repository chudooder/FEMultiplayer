package net.fe.overworldStage.context;

import java.util.Arrays;
import java.util.List;

import net.fe.overworldStage.*;
import net.fe.unit.Unit;
public class UnitMoved extends MenuContext {
	private int origX, origY;
	private Unit unit;
	public UnitMoved(OverworldStage stage, OverworldContext prev, 
			Unit u, int origX, int origY) {
		super(stage, prev, new Menu(0,0));
		menu.x = cursor.x + 16;
		menu.y = cursor.y;
		for(String cmd: getCommands(u)){
			menu.addItem(cmd);
		}
		this.origX = origX;
		this.origY = origY;
		unit = u;
		
		// TODO Auto-generated constructor stub
	}
	
	public List<String> getCommands(Unit u){
		//TODO
		return Arrays.asList("Attack", "Heal", "Item", "Wait");
	}

	@Override
	public void onSelect(String selectedItem) {
		//TODO
		System.out.println("Selected " + selectedItem);
		if(selectedItem.equals("Wait")){
			unit.moved();
			stage.setMenu(null);
			stage.returnToNeutral();
			stage.setContext(new Idle(stage, stage.getPlayer()));
		}
	}

	@Override
	public void onCancel() {
		unit.xcoord = origX;
		unit.ycoord = origY;
		cursor.xcoord = origX;
		cursor.ycoord = origY;
		stage.setMenu(null);
		stage.setContext(prev);
	}

	@Override
	public void onLeft() {
		//Nothing
	}

	@Override
	public void onRight() {
		//Nothing
	}
	
}
