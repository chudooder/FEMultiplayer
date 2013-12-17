package net.fe.overworldStage.context;

import net.fe.overworldStage.Menu;
import net.fe.overworldStage.MenuContext;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.OverworldStage;

public class EndMenu extends MenuContext<String> {

	public EndMenu(OverworldStage stage, OverworldContext prev) {
		super(stage, prev, new Menu<String>());
		menu.addItem("End");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onSelect(String selectedItem) {
		if(menu.getSelection().equals("End")){
			stage.end();
		}
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
