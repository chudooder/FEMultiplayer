package net.fe.overworldStage.context;

import net.fe.overworldStage.MoveZone;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.OverworldStage;

public class UnitSelected extends OverworldContext {
	private MoveZone mz;
	public UnitSelected(OverworldStage s, MoveZone mz) {
		super(s);
		this.mz = mz;
	}

	@Override
	public void onSelect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLeft() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRight() {
		// TODO Auto-generated method stub

	}

}
