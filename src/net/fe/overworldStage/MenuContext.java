package net.fe.overworldStage;

public abstract class MenuContext<T> extends OverworldContext{
	protected Menu<T> menu;
	public MenuContext(OverworldStage stage, OverworldContext prev, Menu<T> m){
		super(stage,prev);
		menu = m;
//		m.x = cursor.x + 17;
//		m.y = cursor.y;
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
	
	public abstract void onSelect(T selectedItem);
	public void onChange(){
		
	}
}
