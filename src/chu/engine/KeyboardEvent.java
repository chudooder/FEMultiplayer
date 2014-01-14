package chu.engine;

public class KeyboardEvent {
	public int key;
	public char eventChar;
	public boolean isRepeatEvent;
	public boolean state;
	public KeyboardEvent(int k, char c, boolean r, boolean s) {
		key = k;
		eventChar = c;
		isRepeatEvent = r;
		state = s;
	}
}
