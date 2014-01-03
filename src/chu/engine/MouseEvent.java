package chu.engine;

public class MouseEvent {
	public int x;
	public int y;
	public int dwheel;
	public int button;
	public boolean state;

	public MouseEvent(int x, int y, int dwheel, int button, boolean s) {
		this.x = x;
		this.y = y;
		this.dwheel = dwheel;
		this.button = button;
		this.state = s;
	}
}
