package chu.engine.menu;

import java.util.List;

import org.lwjgl.input.Mouse;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.MouseEvent;

public abstract class MenuButton extends Entity {
	
	private float width;
	private float height;
	protected boolean hover;

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
	
	public void onEnter(){};
	public void onClick(){};
	public void onExit(){};
	
	public void beginStep() {
		int mX = Mouse.getX() / Game.getScaleX();
		int mY = (Game.getWindowHeight() - Mouse.getY()) / Game.getScaleY();
		boolean newHover = (mX >= x && mX < x + width && mY >= y && mY < y + height);
		if(newHover && !hover) {
			onEnter();
			hover = true;
		} else if(!newHover && hover) {
			onExit();
			hover = false;
		}
		List<MouseEvent> mouseEvents = Game.getMouseEvents();
		for(MouseEvent event : mouseEvents) {
			if(hover && event.button == 0 && event.state) {
				onClick();
				break;
			}
		}
	}
	
	public void endStep() {
		
	}

}
