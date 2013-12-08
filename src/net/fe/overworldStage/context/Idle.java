package net.fe.overworldStage.context;

import net.fe.overworldStage.MoveZone;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.OverworldStage;
import net.fe.unit.Unit;

public class Idle extends OverworldContext {

	public Idle(OverworldStage s) {
		super(s);
	}

	@Override
	public void onSelect() {
		
		Unit u = stage.getUnit(stage.cursor.xcoord, stage.cursor.ycoord);
		if(u!=null){
			stage.addEntity(new MoveZone(stage.grid.getPossibleMoves(u)));
		}

	}

	@Override
	public void onCancel() {
		//Nothing
	}

	@Override
	public void onUp() {
		stage.cursor.ycoord--;
	}

	@Override
	public void onDown() {
		stage.cursor.ycoord++;
	}

	@Override
	public void onLeft() {
		stage.cursor.xcoord--;
	}

	@Override
	public void onRight() {
		stage.cursor.xcoord++;
	}

}
