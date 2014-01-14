package chu.engine.anim;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Tileset {

	private Texture tileset;
	private int tileWidth;
	private int tileHeight;
	private int width;
	private int height;

	public Tileset(Texture t, int tileWidth, int tileHeight) {
		tileset = t;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		width = tileset.getImageWidth();
		height = tileset.getImageHeight();

	}

	public Tileset(String path, int tileWidth, int tileHeight) {
		try {
			tileset = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream(path));
			System.out.println("Loaded: "+path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		width = tileset.getImageWidth();
		height = tileset.getImageHeight();
	}

	public void render(float x, float y, int tx, int ty, float depth) {
		float tx0 = (float) tx / (width / tileWidth);
		float ty0 = (float) ty / (height / tileHeight);
		float tx1 = (float) (tx + 1) / (width / tileWidth);
		float ty1 = (float) (ty + 1) / (height / tileHeight);
		Renderer.render(tileset, tx0, ty0, tx1, ty1, (int) x, (int) y,
				(int) (x + tileWidth), (int) (y + tileHeight), depth);
	}
	
	public void renderTransformed(float x, float y, int tx, int ty, float depth, Transform t) {
		float tx0 = (float) tx / (width / tileWidth);
		float ty0 = (float) ty / (height / tileHeight);
		float tx1 = (float) (tx + 1) / (width / tileWidth);
		float ty1 = (float) (ty + 1) / (height / tileHeight);
		Renderer.render(tileset, tx0, ty0, tx1, ty1, (int) x, (int) y,
				(int) (x + tileWidth), (int) (y + tileHeight), depth, t);
	}

}
