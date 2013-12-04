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
	private static String[] searchFolders = 
		{"battle_anim", "battle_anim/static", "gui"};
	
	
	private static HashMap<String, Audio> audio;
	private static HashMap<String, TextureData> textures;
	private static HashMap<String, TrueTypeFont> fonts;
	
	static {
		audio = new HashMap<String, Audio>();
		textures = new HashMap<String, TextureData>() {
			@Override
			public TextureData put(String string, TextureData data) {
				System.out.println(string + "(texture) loaded.");
				return super.put(string, data);
			}
		};
		fonts = new HashMap<String, TrueTypeFont>() {
			@Override
			public TrueTypeFont put(String string, TrueTypeFont data) {
				System.out.println(string + "(font) loaded.");
				return super.put(string, data);
			}
		};
		try {
			// Textures
			textures.put("whoops", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/whoops.png")),
					32, 32, 1, 1, 0, 0, -1));
			textures.put("roy_sword_critical", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/roy_sword_critical.png")),
					142, 102, 96, 12, 75, 99, 0, 47));
			textures.put("roy_sword_attack", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/roy_sword_attack.png")),
					124, 102, 82, 11, 76, 100, 0, 34));
			textures.put("roy_sword_dodge", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/roy_sword_dodge.png")),
					38, 33, 2, 2, 16, 29, 0));
			textures.put("eliwood_lance_attack", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/eliwood_lance_attack.png")),
					143, 89, 35, 6, 100, 81, 0, 16));
			textures.put("eliwood_lance_critical", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/eliwood_lance_critical.png")),
					153, 82, 40, 7, 95, 74, 0, 22));
			textures.put("eliwood_lance_dodge", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/eliwood_lance_dodge.png")),
					44, 53, 2, 2, 22, 48, 0));
			textures.put("eliwood_sword_attack", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/eliwood_sword_attack.png")),
					144, 106, 40, 7, 100, 101, 0, 21));
			textures.put("eliwood_sword_critical", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/eliwood_sword_critical.png")),
					205, 126, 59, 8, 138, 101, 0, 41));
			textures.put("eliwood_sword_dodge", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/eliwood_sword_dodge.png")),
					60, 49, 2, 2, 39, 46, 0));
			textures.put("sage_magic_attack", new TextureData(
					TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(
							"res/battle_anim/sage_magic_attack.png")),
					74, 60, 38, 8, 30, 43, 8, 16));
			textures.put("sage_magic_critical", new TextureData(
					TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(
							"res/battle_anim/sage_magic_critical.png")),
					74, 60, 60, 8, 30, 43, 8, 38));
			textures.put("sage_magic_dodge", new TextureData(
					TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(
							"res/battle_anim/sage_magic_dodge.png")),
					28, 35, 2, 2, 18, 31, 0));
			textures.put("sage_staff_attack", new TextureData(
					TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(
							"res/battle_anim/sage_staff_attack.png")), 
					34, 41, 5, 5, 21, 37, 0, 2));
			
			
			textures.put("hit_effect_attack", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/hit_effect_attack.png")),
					240, 160, 9, 3, 0, 0, -1));
			textures.put("hit_effect_critical", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/hit_effect_critical.png")),
					240, 160, 9, 3, 0, 0, -1));
			textures.put("hit_effect_elfire", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/hit_effect_elfire.png")),
					240, 160, 15, 4, 0, 0, -1));
			textures.put("miss", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/miss.png"))));
			
			textures.put("magic_effect_elfire", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/magic_effect_elfire.png")),
					240, 160, 12, 4, 0, 0, -1));
			
			
			textures.put("gui_tickEmpty", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/gui/tickEmpty.png"))));
			textures.put("gui_tickFilled", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/gui/tickFilled.png"))));
			
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
			//try to get it, in case we forgot
			System.out.println("Warn:" + string + " not explicitly defined");
			for(String loc: searchFolders){
				try{
					TextureData txt = new TextureData(TextureLoader.getTexture("PNG",
							ResourceLoader.getResourceAsStream(
								"res/" + loc + "/" + string + ".png"
							)));
					textures.put(string, txt);
					return txt;
				} catch (Exception e){
					
				}
			}
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
