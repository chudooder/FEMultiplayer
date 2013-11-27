package chu.engine;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Resources {
	
	private static HashMap<String, Audio> audio;
	private static HashMap<String, Texture> textures;
	
	static {
		audio = new HashMap<String, Audio>();
		textures = new HashMap<String, Texture>();
		try {
			textures.put("ROY_ATTACK", TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/ROY_ATTACK.png")));
			textures.put("gui_tickEmpty", TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_gui/tickEmpty.png")));
			textures.put("gui_tickFilled", TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_gui/tickFilled.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Texture getTexture(String string) {
		return textures.get(string);
	}

}
