package chu.engine;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public 
class AnimationData {
	public String path;
	public int frames;
	public int columns;
	public int frameWidth;
	public int frameHeight;
	public int offsetX;
	public int offsetY;
	public int freeze;
	public int[] hitframes;
	public HashMap<Integer, String> soundMap;
	public float speed;
	public int shakeFrames;
	public int shakeIntensity;
	public boolean stop;
	
	public AnimationData(String path, int w, int h, int r, int c, int x, int y,
			int f, int[] frames, HashMap<Integer, String> soundMap) {
		this.path = path;
		this.frames = r;
		columns = c;
		offsetX = x;
		offsetY = y;
		frameWidth = w;
		frameHeight = h;
		freeze = f;
		hitframes = frames;
		this.soundMap = soundMap;
	}
	
	public AnimationData(String path) {
		this.path = path;
		frames = 1;
		columns = 1;
		offsetX = 0;
		offsetY = 0;
		freeze = -1;
	}
	
	public Texture getTexture() {
		try {
			Texture t = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
			System.out.println("Loaded "+path);
			return t;
		} catch (IOException e) {
			System.err.println("Texture not found: "+path);
			e.printStackTrace();
			return null;
		}
	}
}
