package chu.engine;

/**
 * Abstract class that determines how an object deals with collisions.
 * Entities without defined hitboxes cannot collide with other objects.
 * Entities with defined hitboxes will consult their hitbox for collision
 * detection. Child classes of Hitbox define how the entity checks for
 * collision.
 * @author Shawn
 *
 */

public class Hitbox {
	
	protected Entity parent;
	protected float offsetX;
	protected float offsetY;
	
	public Hitbox(Entity p, int x, int y) {
		parent = p;
		offsetX = x;
		offsetY = y;
	}
	
	public float getX() {
		return parent.x + offsetX;
	}
	
	public float getY() {
		return parent.y + offsetY;
	}
	
	public static int collisionExists(Entity e, Entity f, int x, int y) {
		Hitbox a = e.hitbox;
		Hitbox b = f.hitbox;
		if(a instanceof RectangleHitbox) {
			RectangleHitbox r1 = (RectangleHitbox)a;
			if(b instanceof RectangleHitbox) {
				RectangleHitbox r2 = (RectangleHitbox)b;
				return checkCollision(r1, r2, x, y);
			} else if(b instanceof LineHitbox) {
				LineHitbox l2 = (LineHitbox)b;
				return checkCollision(r1, l2, x, y);
			}
		} else if(a instanceof LineHitbox) {
			LineHitbox l1 = (LineHitbox)a;
			if(b instanceof RectangleHitbox) {
				RectangleHitbox r2 = (RectangleHitbox)b;
				return checkCollision(r2, l1, x, y);
			} else if(b instanceof LineHitbox) {
				LineHitbox l2 = (LineHitbox)b;
				return checkCollision(l1, l2, x, y);
			}
		}
		
		return -1;
	}
	
	public static int collisionExists(Entity e, Entity f) {
		return collisionExists(e, f, 0, 0);
	}
	
	

	//@Override
	public static int checkCollision(RectangleHitbox rect, LineHitbox line, int offsetX, int offsetY) {
		//Liang-Barsky algorithm incoming.
		int x0 = (int)line.getX();
		int y0 = (int)line.getY();
		int x1 = (int)line.getEndX();
		int y1 = (int)line.getEndY();
		int xmin = (int)(rect.getX() + offsetX);
		int ymin = (int)(rect.getY() + offsetY);
		int xmax = (int)(xmin + rect.getWidth());
		int ymax = (int)(ymin + rect.getHeight());
		int dx = x1 - x0;
		int dy = y1 - y0;
		
		double u0 = 0;		//For a line defined as x = x0 + u*dx, and y = y0 + u*dy
		double u1 = 1;		//basically how far along the line the intersection is
		
		for(int k=0; k<4; k++) {
			double p,q;
			if(k == 0) {		//Left
				p = -dx;
				q = x0 - xmin;
			} else if(k == 1) {	//Right
				p = dx;
				q = xmax - x0;
			} else if(k == 2) {	//Up
				p = -dy;
				q = y0 - ymin;
			} else {			//Down
				p = dy;
				q = ymax - y0;
			}
			
			//Check for parallel lines (p == 0)
			if(p == 0) {
				//If q < 0, then the line is completely outside: eliminate
				if(q < 0) return -1;
			} else {
				double r = q/p;
				//If p < 0, then the line goes from outside to inside.
				if(p < 0) {
					if(r > u1) return -1;
					if(r > u0) u0 = r;
				}
				
				//If p > 0, then the line goes from inside to outside.
				if(p > 0) {
					if(r < u0) return -1;
					if(r < u1) u1 = r;
				}
			}
		}
		
		return 1;
	}
	
	public static double getIntersection(RectangleHitbox rect, LineHitbox line, int offsetX, int offsetY) {
		//Liang-Barsky algorithm incoming.
		float x0 = line.getX();
		float y0 = line.getY();
		float x1 = line.getEndX();
		float y1 = line.getEndY();
		float xmin = rect.getX() + offsetX;
		float ymin = rect.getY() + offsetY;
		float xmax = xmin + rect.getWidth();
		float ymax = ymin + rect.getHeight();
		float dx = x1 - x0;
		float dy = y1 - y0;
		
		double u0 = 0;		//For a line defined as x = x0 + u*dx, and y = y0 + u*dy
		double u1 = 1;		//basically how far along the line the intersection is
		
		for(int k=0; k<4; k++) {
			double p,q;
			if(k == 0) {		//Left
				p = -dx;
				q = x0 - xmin;
			} else if(k == 1) {	//Right
				p = dx;
				q = xmax - x0;
			} else if(k == 2) {	//Up
				p = -dy;
				q = y0 - ymin;
			} else {			//Down
				p = dy;
				q = ymax - y0;
			}
			
			//Check for parallel lines (p == 0)
			if(p == 0) {
				//If q < 0, then the line is completely outside: eliminate
				if(q < 0) return -1;
			} else {
				double r = q/p;
				//If p < 0, then the line goes from outside to inside.
				if(p < 0) {
					if(r > u1) return -1;
					if(r > u0) u0 = r;
				}
				
				//If p > 0, then the line goes from inside to outside.
				if(p > 0) {
					if(r < u0) return -1;
					if(r < u1) u1 = r;
				}
			}
		}
		
		return u0;
	}
	
	private static int checkCollision(RectangleHitbox a, RectangleHitbox b, int offsetX, int offsetY) {
		if((int)(a.getX()+offsetX) >= (int)(b.getX() + b.getWidth())) return -1;
		if((int)(a.getX()+offsetX + a.getWidth()) <= (int)(b.getX())) return -1;
		if((int)(a.getY()+offsetY) >= (int)(b.getY() + b.getHeight())) return -1;
		if((int)(a.getY()+offsetY + a.getHeight()) <= (int)(b.getY())) return -1;
		
		return 1;
	}
	
	private static int checkCollision(LineHitbox a, LineHitbox b, int offsetX, int offsetY) {
		float first, second;
		float aX = a.getX() + offsetX;
		float aY = a.getY() + offsetY;
		float bX = a.getEndX() + offsetX;
		float bY = a.getEndY() + offsetX;
		float cX = b.getX();
		float cY = b.getY();
		float dX = b.getEndX();
		float dY = b.getEndY();
		
		first = aX*(bY-cY) + bX*(cY-aY) + cX*(aY-bY);
		second = aX*(bY-dY) + bX*(dY-aY) + dX*(aY-bY);
		
		if(first < 0 && second < 0) return -1;
		if(first > 0 && second > 0) return -1;
		return 1;
		
	}

}
