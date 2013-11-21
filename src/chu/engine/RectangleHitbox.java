package chu.engine;

public class RectangleHitbox extends Hitbox {
	
	private float width;
	private float height;

	public RectangleHitbox(Entity p, int x, int y, int w, int h) {
		super(p, x, y);
		setWidth(w);
		height = h;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public float getEndY() {
		return getY() + height;
	}
	
	public float getEndX() {
		return getX() + width;
	}
	
}
