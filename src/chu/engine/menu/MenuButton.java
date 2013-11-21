package chu.engine.menu;

import java.util.HashMap;

import org.lwjgl.input.Mouse;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.MouseEvent;

public abstract class MenuButton extends Entity {
	
	private float width;
	private float height;
	private boolean hover;

	public MenuButton(float x, float y) {
		super(x, y);
		width = 0;
		height = 0;
		hover = false;
	}
	
	public MenuButton(float x, float y, float w, float h) {
		this(x, y);
		width = w;
		height = h;
	}
	
	public abstract void onEnter();
	public abstract void onClick();
	public abstract void onExit();
	
	public void beginStep() {
		int mX = Mouse.getX();
		int mY = 480 - Mouse.getY();
		boolean newHover = (mX >= x && mX < x + width && mY >= y && mY < y + height);
		if(newHover && !hover) {
			onEnter();
			hover = true;
		} else if(!newHover && hover) {
			onExit();
			hover = false;
		}
		HashMap<MouseEvent, Boolean> mouseEvents = Game.getMouseEvents();
		for(MouseEvent event : mouseEvents.keySet()) {
			if(hover && event.button == 0 && mouseEvents.get(event)) {
				onClick();
				break;
			}
		}
	}
	
	public void endStep() {
		
	}

}
