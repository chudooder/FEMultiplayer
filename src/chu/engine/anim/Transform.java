package chu.engine.anim;

import org.newdawn.slick.Color;

public class Transform {
	
	public float rotation;
	public float translateX;
	public float translateY;
	public float scaleX;
	public float scaleY;
	public boolean flipHorizontal;
	public boolean flipVertical;
	public Color color;
	
	public Transform() {
		rotation = 0;
		translateX = 0;
		translateY = 0;
		scaleX = 1;
		scaleY = 1;
		flipHorizontal = false;
		flipVertical = false;
		color = Color.white;
	}
	
	public void setRotation(float angle) {
		rotation = angle;
	}
	
	public void setTranslation(float x, float y) {
		translateX = x;
		translateY = y;
	}
	
	public void setScale(float x, float y) {
		scaleX = x;
		scaleY = y;
	}
	
	public void flipHorizontal() {
		flipHorizontal = true;
	}

	public void flipVertical() {
		flipVertical = true;
	}
	
	public void setColor(Color c) {
		color = c;
	}
}
