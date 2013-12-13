package chu.engine.anim;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;

public class Animation {
	private Texture texture;
	private int width;
	private int height;
	private int rows;
	private int columns;
	private int currentFrame;
	private int counter;
	private int length;
	private int offsetX;
	private int offsetY;
	protected Sprite sprite;
	protected int speed;			//Time for each frame in milliseconds
	
	public Animation(Texture t) {
		texture = t;
		width = t.getImageWidth();
		height = t.getImageHeight();
		length = 1;
		rows = 1;
		columns = 1;
		speed = 0;
	}
	
	public Animation(Texture t, int width, int height, int length, int columns, int speed) {
		this(t);
		this.width = width;
		this.height = height;
		this.columns = columns;
		this.rows = (length/columns)+1;
		this.length = length;
		this.speed = speed;
		this.offsetX = 0;
		this.offsetY = 0;
	}
	
	public Animation(Texture t, int width, int height, int length, int columns, int offsetX, int offsetY, int speed) {
		this(t);
		this.width = width;
		this.height = height;
		this.columns = columns;
		this.rows = (length/columns)+1;
		this.length = length;
		this.speed = speed;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	public int getLength() { return length; }
	public int getFrame() { return currentFrame; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getRows() { return rows; }
	public int getColumns() { return columns; }
	public int getImageHeight() { return texture.getImageHeight(); }
	public int getImageWidth() { return texture.getImageWidth(); }
	public Texture getTexture() { return texture; }
	
	
	public void update() {
		if(speed != 0) counter += Game.getDelta();
		if(speed != 0 && counter/1000000 > Math.abs(speed)) {
			increment();
			counter = 0;
		}
	}
	
	public void increment() {
		if(speed > 0) {
			currentFrame += 1;
			if(currentFrame >= length) done();
		} else {
			currentFrame -= 1;
			if(currentFrame < 0) done();
		}
	}
	
	public void done() {
		if(speed > 0)
			currentFrame = 0;
		else
			currentFrame = columns-1;
	}

	public void setFrame(int i) {
		this.currentFrame = i;
	}

	public void setSpeed(int i) {
		speed = i;
	}

	public void setSprite(Sprite sprite2) {
		sprite = sprite2;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public int getOffsetX() {
		return offsetX;
	}
	
	public int getOffsetY() {
		return offsetY;
	}

}
