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
			loadTextures();

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
			audio.put("kuritiku_hittu", AudioLoader.getAudio("WAV",
					ResourceLoader.getResourceAsStream("res/battle_sounds/kuritiku.wav")));
			
			
		} catch (IOException e) {
			int max = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
			System.out.println(max);
			e.printStackTrace();
		}
	}

	public static Texture getTexture(String string) {
		return getTextureData(string).texture;
	}
	
	private static void loadTextures() {
		// TODO Load textures from JSON
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
