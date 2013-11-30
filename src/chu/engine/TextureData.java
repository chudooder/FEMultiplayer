package chu.engine;

import org.newdawn.slick.opengl.Texture;

public 
class TextureData {
	public Texture texture;
	public int frames;
	public int columns;
	public int frameWidth;
	public int frameHeight;
	public int offsetX;
	public int offsetY;
	public int freeze;
	public int[] hitframes;
	
	public TextureData(Texture t, int w, int h, int r, int c, int x, int y, int f, int... frames) {
		texture = t;
		this.frames = r;
		columns = c;
		offsetX = x;
		offsetY = y;
		frameWidth = w;
		frameHeight = h;
		freeze = f;
		hitframes = frames;
	}
	
	public TextureData(Texture t) {
		texture = t;
		frames = 1;
		columns = 1;
		offsetX = 0;
		offsetY = 0;
		freeze = -1;
		frameWidth = t.getImageWidth();
		frameHeight = t.getImageHeight();
	}
}
