package chu.engine.anim;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;

public class Animation {
	private Texture texture;
	private int width;
	private int height;
	private int length;
	private int currentFrame;
	private int counter;
	private int[][] offset;
	protected int speed;			//Time for each frame in milliseconds
	
	public Animation(Texture t) {
		texture = t;
		width = t.getImageWidth();
		height = t.getImageHeight();
		offset = new int[2][length];
		length = 1;
		speed = 0;
	}
	
	public Animation(Texture t, int width, int height, int frames, int speed) {
		this(t);
		this.width = width;
		this.height = height;
		this.length = frames;
		this.speed = speed;
	}
	
	public int getLength() { return length; }
	public int getFrame() { return currentFrame; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getImageWidth() { return texture.getImageWidth(); }
	public Texture getTexture() { return texture; }
	public int getOffsetX(int frame) {
		return offset[0][frame];
	}
	public int getOffsetY(int frame) {
		return offset[1][frame];
	}
	
	public void setOffset(int frame, int x, int y) {
		offset[0][frame] = x;
		offset[1][frame] = y;
	}
	
	
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
			currentFrame = length-1;
	}

	public void setFrame(int i) {
		this.currentFrame = i;
	}

	public void setSpeed(int i) {
		speed = i;
	}
	

}
