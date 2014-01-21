package chu.engine.anim;

import static org.lwjgl.opengl.GL11.glTranslatef;
import chu.engine.Entity;
import chu.engine.Game;

public class Camera {
	
	Entity center;
	int offsetX;
	int offsetY;
	
	public Camera(Entity e, int oX, int oY) {
		set(e, oX, oY);
	}
	
	public void set(Entity e, int oX, int oY) {
		center = e;
		offsetX = oX;
		offsetY = oY;
	}
	
	public void lookThrough() {
		if(center != null) {
			glTranslatef(-(center.x + offsetX - Game.getWindowWidth()/2), 
					-(center.y + offsetY - Game.getWindowHeight()/2), 0);
		} 
	}
	
	public void lookBack() {
		if(center != null) {
			glTranslatef(center.x + offsetX - Game.getWindowWidth()/2, 
					center.y + offsetY - Game.getWindowHeight()/2, 0);
		}
	}
	
	public float getX() {
		if(center != null)
			return center.x + offsetX;
		else
			return Game.getWindowWidth()/2;
	}
	
	public float getY() {
		if(center != null)
			return center.y + offsetY;
		else
			return Game.getWindowHeight()/2;
	}
	
	public float getScreenX() {
		if(center != null)
			return center.x + offsetX - Game.getWindowWidth()/2;
		else
			return 0;
	}
	
	public float getScreenY() {
		if(center != null)
			return center.y + offsetY - Game.getWindowHeight()/2;
		else
			return 0;
	}

}
