package chu.engine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.opengl.renderer.Renderer;

import chu.engine.anim.BitmapFont;

public class Resources {
	private static String[] searchFolders = 
		{"battle_anim", "battle_anim/static", "gui"};
	
	
	private static HashMap<String, Audio> audio;
	private static HashMap<String, TextureData> textures;
	private static HashMap<String, TrueTypeFont> fonts;
	private static HashMap<String, BitmapFont> bitmapFonts;
	
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
		bitmapFonts = new HashMap<String, BitmapFont>();
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
			textures.put("lyn_sword_attack", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/lyn_sword_attack.png")),
					118, 119, 37, 7, 83, 101, 0, 13));
			textures.put("lyn_sword_critical", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/lyn_sword_critical.png")),
					220, 144, 115, 9, 137, 92, -1, 34, 37, 40, 43, 46, 49, 63));
			textures.put("lyn_sword_dodge", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/lyn_sword_dodge.png")),
					25, 34, 2, 2, 9, 33, 0));
			textures.put("lyn_bow_attack", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/lyn_bow_attack.png")),
					40, 52, 13, 4, 25, 49, 0, 12));
			textures.put("lyn_bow_critical", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/lyn_bow_critical.png")),
					47, 52, 24, 5, 25, 49, 0, 23));
			textures.put("lyn_bow_dodge", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/lyn_bow_dodge.png")),
					36, 33, 2, 2, 19, 28, 0));
			
			
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
			textures.put("sage_staff_dodge", new TextureData(
					TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(
							"res/battle_anim/sage_staff_dodge.png")),
					36, 35, 2, 2, 19, 31, 0));
			
			textures.put("assassin_sword_attack", new TextureData(
					TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(
							"res/battle_anim/assassin_sword_attack.png")),
					90, 43, 47, 7, 68, 40, 0, 21));
			textures.put("assassin_sword_critical", new TextureData(
					TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(
							"res/battle_anim/assassin_sword_critical.png")),
					123, 108, 61, 8, 101, 96, 0, 39));
			textures.put("assassin_sword_dodge", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/assassin_sword_dodge.png")),
					33, 34, 2, 2, 17, 31, 0));
			
			
			textures.put("hit_effect_attack", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/hit_effect_attack.png")),
					240, 160, 9, 3, 88, 110, -1));
			textures.put("hit_effect_critical", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/hit_effect_critical_alt2.png")),
					240, 160, 9, 3, 88, 110, -1));
			textures.put("hit_effect_elfire", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/hit_effect_elfire.png")),
					240, 160, 15, 4, 88, 110, -1));
			textures.put("hit_effect_heal", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/hit_effect_heal.png")),
					72, 72, 30, 6, 46, 46, -1));
			textures.put("miss", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/miss.png"))));
			
			textures.put("magic_effect_elfire", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/magic_effect_elfire.png")),
					240, 160, 12, 4, 160, 104, -1));
			textures.put("bg_effect_elfire", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/battle_anim/bg_effect_elfire.png")),
					1, 1, 32, 16, 0, 0, -1));
			textures.put("roy_sword_critical", new TextureData(
					TextureLoader.getTexture("PNG",	ResourceLoader.getResourceAsStream(
							"res/battle_anim/roy_sword_critical.png")),
					142, 102, 96, 12, 75, 99, 0, 47));
			

			textures.put("zone_colors", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/gui/zone_colors.png"))));
			textures.put("gui_selectArrow", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/gui/selectArrow.png")),
					8, 8, 6, 6, 0, 0, -1));
			textures.put("gui_tickEmpty", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/gui/tickEmpty.png"))));
			textures.put("gui_tickFilled", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/gui/tickFilled.png"))));
			textures.put("gui_weaponIcon", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/weaponIcon.png"))));
			textures.put("gui_battleStats", new TextureData(TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/gui/battleStats.png"))));
			
			//Load bitmap fonts
			loadBitmapFonts();
			
			//load audio
			audio.put("hit0", AudioLoader.getAudio("WAV",
					ResourceLoader.getResourceAsStream("res/battle_sounds/hit1.wav")));
			audio.put("hit1", AudioLoader.getAudio("WAV",
					ResourceLoader.getResourceAsStream("res/battle_sounds/hit2.wav")));
			audio.put("hit2", AudioLoader.getAudio("WAV",
					ResourceLoader.getResourceAsStream("res/battle_sounds/hit3.wav")));
			audio.put("crit", AudioLoader.getAudio("WAV",
					ResourceLoader.getResourceAsStream("res/battle_sounds/crit.wav")));
			
			
		} catch (IOException e) {
			int max = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
			System.out.println(max);
			e.printStackTrace();
		}
	}

	public static Texture getTexture(String string) {
		return getTextureData(string).texture;
	}
	
	public static BitmapFont getBitmapFont(String name) {
		return bitmapFonts.get(name);
	}
	
	private static void loadBitmapFonts() {
		Scanner in = new Scanner(ResourceLoader.getResourceAsStream("res/fonts/fonts.txt"));
		while(in.hasNextLine()) {
			String line = in.nextLine();
			if(line.startsWith("#"))
				continue;
			if(line.startsWith("define")) {
				String name = line.split(":")[1];
				String texName = in.nextLine();
				char[] chars = in.nextLine().toCharArray();
				int height = Integer.parseInt(in.nextLine());
				int spacing = Integer.parseInt(in.nextLine());
				char[] widths = in.nextLine().toCharArray();
				
				BitmapFont font = new BitmapFont(texName);
				font.setHeight(height);
				font.setSpacing(spacing);
				int pos = 0;
				for(int i=0; i<chars.length; i++) {
					int width = Integer.parseInt(widths[i]+"");
					font.put(chars[i], pos, width);
					pos += width;
				}
				bitmapFonts.put(name, font);
				System.out.println(name+"(bitmap font) loaded");
			}
		}
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
	
	public static TrueTypeFont createFontFromFile(String name, int size) {
		try {
			Font awtfont = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("res/"+name+".ttf"));
			awtfont = awtfont.deriveFont(Font.TRUETYPE_FONT, size);
			TrueTypeFont ttf = new TrueTypeFont(awtfont, false);
			return ttf;
		} catch (FontFormatException e) {
			System.err.println("Font "+name+" could not be created!");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("Font "+name+" could not be found!");
			e.printStackTrace();
			return null;
		}
	}

	public static TrueTypeFont getFont(String fontName) {
		return fonts.get(fontName);
	}

	public static Audio getAudio(String name) {
		return audio.get(name);
	}

}
