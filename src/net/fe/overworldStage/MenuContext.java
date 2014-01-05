package net.fe.overworldStage;

public abstract class MenuContext<T> extends OverworldContext{
	protected Menu<T> menu;
	public MenuContext(ClientOverworldStage stage, OverworldContext prev, Menu<T> m){
		super(stage,prev);
		menu = m;
		//TODO Position
		m.x = 225;
		m.y = 0;
		if(cursor.getXCoord() > stage.grid.width - 5){
			m.x = cursor.x - m.width;
		}
		
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
		
	}
}
