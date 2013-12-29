package net.fe.editor;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class LevelEditorStage extends Stage {
	
	private static Texture palette;
	
	static {
		try {
			palette = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/terrain_tiles.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void beginStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void render() {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			Renderer.render(palette, 0, 0, 1, 1, 0, 0, 270, 270, 0);
	}

}
