package net.fe.overworldStage;

public abstract class MenuContext extends OverworldContext{
	protected Menu menu;
	public MenuContext(OverworldStage stage, OverworldContext prev, Menu m){
		super(stage,prev);
		stage.setMenu(m);
		menu = m;
	}

	public void onSelect(){
		onSelect(menu.getSelection());
	}
	
	@Override
	public void onUp() {
		menu.up();	
	}

	@Override
	public void onDown() {
		menu.down();
	}
	
	public abstract void onSelect(String selectedItem);
}
