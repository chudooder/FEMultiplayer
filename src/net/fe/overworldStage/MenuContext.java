package net.fe.overworldStage;

import chu.engine.anim.AudioPlayer;

public abstract class MenuContext<T> extends OverworldContext{
	protected Menu<T> menu;
	public MenuContext(ClientOverworldStage stage, OverworldContext prev, Menu<T> m){
		super(stage,prev);
		menu = m;
		m.x = ClientOverworldStage.RIGHT_AXIS - menu.getWidth()/2;
		m.y = 75;
		
	}
	
	public void startContext(){
		super.startContext();
		cursor.off();
		stage.setMenu(menu);
	}
	
	public void cleanUp(){
		cursor.on();
		stage.setMenu(null);
	}

	public void onSelect(){
		onSelect(menu.getSelection());
	}
	
	@Override
	public void onUp() {
		menu.up();
		onChange();
	}

	@Override
	public void onDown() {
		menu.down();
		onChange();
	}
	
	public abstract void onSelect(T selectedItem);
	public void onChange(){
		AudioPlayer.playAudio("cursor2", 1, 1);
	}
}
