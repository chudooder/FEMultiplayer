package chu.engine.menu;

import java.util.List;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.network.message.ChatMessage;

import org.lwjgl.input.Keyboard;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.MouseEvent;
import chu.engine.anim.BitmapFont;

public abstract class TextInputBox extends Entity {

	protected float width;
	protected float height;
	protected boolean hasFocus;
	protected int cursorPos;
	protected StringBuilder input;
	protected BitmapFont font;
	
	public TextInputBox(float x, float y, float w, float h, String fontname) {
		super(x, y);
		width = w;
		height = h;
		hasFocus = false;
		cursorPos = 0;
		input = new StringBuilder();
		font = FEResources.getBitmapFont(fontname);
	}
	
	public void beginStep() {
		if(hasFocus) {
			List<KeyboardEvent> keys = Game.getKeys();
			for(KeyboardEvent ke : keys) {
				if(ke.state) {
					char c = ke.eventChar;
					if(isValidCharacter(c) && FEResources.getBitmapFont("default_med")
							.getStringWidth(input.toString()+c) < 246) {
						input.insert(cursorPos, c);
						cursorPos++;
					} else {
						if(ke.key == Keyboard.KEY_LEFT && cursorPos > 0) { 
							cursorPos--;
						}
						else if(ke.key == Keyboard.KEY_RIGHT && cursorPos < input.length()) { 
							cursorPos++;
						}
						else if(ke.key == Keyboard.KEY_BACK && cursorPos > 0) { 
							input.deleteCharAt(cursorPos-1);
							cursorPos--;
						}
						else if(ke.key == Keyboard.KEY_DELETE && cursorPos < input.length()) { 
							input.deleteCharAt(cursorPos);
						}
					}
				}
			}
		}
	}
	
	protected boolean inBounds(float otherX, float otherY) {
		if(otherX < x || otherX >= x+width 
				|| otherY < y || otherY >= y+height) {
			return false;
		}
		return true;
	}
	
	private boolean isValidCharacter(char c) {
		return font.containsCharacter(c);
	}

	public String getInput() {
		return input.toString();
	}
}
