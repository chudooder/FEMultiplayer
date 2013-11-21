package chu.engine;

public class LineHitbox extends Hitbox {

	protected float dx;
	protected float dy;
	
	public LineHitbox(Entity p, int offsetX, int offsetY, int dx, int dy) {
		super(p, offsetX, offsetY);
		this.dx = dx;
		this.dy = dy;
	}
	
	public float getEndX() {
		return parent.x + offsetX + dx;
	}
	
	public float getEndY() {
		return parent.y + offsetY + dy;
	}

}
