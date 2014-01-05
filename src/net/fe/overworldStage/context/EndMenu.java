package net.fe.overworldStage.context;

import chu.engine.anim.AudioPlayer;
import net.fe.overworldStage.Menu;
import net.fe.overworldStage.MenuContext;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;

public class EndMenu extends MenuContext<String> {

	public EndMenu(ClientOverworldStage stage, OverworldContext prev) {
		super(stage, prev, new Menu<String>());
		menu.addItem("End");
	}

	@Override
	public void onSelect(String selectedItem) {
		AudioPlayer.playAudio("select", 1, 1);
		if(menu.getSelection().equals("End")){
			stage.end();
		}
	}

	@Override
	public void onLeft() {
		
	}

	@Override
	public void onRight() {
		
	}

}
