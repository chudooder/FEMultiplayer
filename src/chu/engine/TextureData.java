package chu.engine;

import org.newdawn.slick.opengl.Texture;

public 
class TextureData {
	public Texture texture;
	public int rows;
	public int columns;
	public int frameWidth;
	public int frameHeight;
	public int[] hitframes;
	
	public TextureData(Texture t, int w, int h, int r, int c, int... frames) {
		texture = t;
		rows = r;
		columns = c;
		frameWidth = w;
		frameHeight = h;
		hitframes = frames;
	}
}
