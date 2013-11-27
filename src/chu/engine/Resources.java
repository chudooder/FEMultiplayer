package chu.engine;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Resources {
	
	private static HashMap<String, Audio> audio;
	private static HashMap<String, TextureData> textures;
	
	static {
		audio = new HashMap<String, Audio>();
		textures = new HashMap<String, TextureData>();
		try {
			textures.put("whoops", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/whoops.png")),
					32, 32, 1, 1));
			textures.put("roy_sword_crit", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/roy_sword_crit.png")),
					142, 102, 1, 96, 47));
			textures.put("roy_sword_attack", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/roy_sword_attack.png")),
					124, 102, 1, 82, 34));
			textures.put("gui_tickEmpty", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_gui/tickEmpty.png"))));
			textures.put("gui_tickFilled", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_gui/tickFilled.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Texture getTexture(String string) {
		return getTextureData(string).texture;
	}
	
	public static TextureData getTextureData(String string) {
		TextureData t = textures.get(string);
		if(t != null) {
			return t;
		} else {
			return textures.get("whoops");
		}
	}

}
