package chu.engine;

import org.newdawn.slick.opengl.Texture;

public 
class TextureData {
	public Texture texture;
	public int frames;
	public int columns;
	public int frameWidth;
	public int frameHeight;
	public int headX;
	public int headY;
	public int freeze;
	public int[] hitframes;
	
	public TextureData(Texture t, int w, int h, int r, int c, int x, int y, int f, int... frames) {
		texture = t;
		this.frames = r;
		columns = c;
		headX = x;
		headY = y;
		frameWidth = w;
		frameHeight = h;
		freeze = f;
		hitframes = frames;
	}
	
	public TextureData(Texture t) {
		texture = t;
		frames = 1;
		columns = 1;
		headX = 0;
		headY = 0;
		freeze = -1;
		frameWidth = t.getImageWidth();
		frameHeight = t.getImageHeight();
	}
}
