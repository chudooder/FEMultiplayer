package chu.engine;

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.opengl.renderer.Renderer;

public class Resources {
	
	private static HashMap<String, Audio> audio;
	private static HashMap<String, TextureData> textures;
	private static HashMap<String, TrueTypeFont> fonts;
	
	static {
		audio = new HashMap<String, Audio>();
		textures = new HashMap<String, TextureData>();
		fonts = new HashMap<String, TrueTypeFont>();
		try {
			// Textures
			textures.put("whoops", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/whoops.png")),
					32, 32, 1, 1));
			textures.put("roy_sword_crit", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/roy_sword_crit.png")),
					142, 102, 96, 12, 47));
			textures.put("roy_sword_attack", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/roy_sword_attack.png")),
					124, 102, 82, 11, 34));
			textures.put("gui_tickEmpty", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_gui/tickEmpty.png"))));
			textures.put("gui_tickFilled", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_gui/tickFilled.png"))));
			textures.put("hit_effect_attack", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/hit_effect.png")),
					240, 160, 9, 3));
			textures.put("hit_effect_crit", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/hit_effect_crit.png")),
					240, 160, 9, 3));
			// Fonts
			fonts.put("default", createFont("Arial Narrow", Font.PLAIN, 11));
			fonts.put("default_small", createFont("Arial Narrow", Font.PLAIN, 9));
			fonts.put("number", createFont("Consolas", Font.BOLD, 10));
		} catch (IOException e) {
			int max = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
			System.out.println(max);
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
	
	public static TrueTypeFont createFont(String name, int type, int size) {
		Font awtfont = new Font(name, type, size);
		return new TrueTypeFont(awtfont, false);
	}

	public static TrueTypeFont getFont(String fontName) {
		return fonts.get(fontName);
	}

}
