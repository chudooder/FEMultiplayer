package net.fe.overworldStage;

public abstract class MenuContext extends OverworldContext{
	protected Menu menu;
	public MenuContext(OverworldStage stage, OverworldContext prev, Menu m){
		super(stage,prev);
		menu = m;
	}
	
	public void startContext(){
		super.startContext();
		stage.setMenu(menu);
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
	
	public abstract void onSelect(String selectedItem);
	public void onChange(){
		
	}
}
