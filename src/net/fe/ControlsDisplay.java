package net.fe;

import java.util.Map.Entry;
import java.util.*;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class ControlsDisplay extends Entity{
	private Map<String, String> keys;
	public ControlsDisplay() {
		super(0, 300);
		renderDepth = 0;
		keys = new LinkedHashMap<String, String>();
	}
	public void render(){
		Renderer.drawRectangle(x, y, x+480, y+20, renderDepth, new Color(0,0,0,0.5f));
		StringBuilder str = new StringBuilder();
		String sep = "   |   ";
		for(Entry<String, String> entry: keys.entrySet()){
			str.append(sep);
			str.append(entry.getKey() + ": " + entry.getValue());
		}
		String s = str.substring(sep.length());
		int width = FEResources.getBitmapFont("default_med").getStringWidth(s);
		Renderer.drawString("default_med", s, 240 - width/2, y+5, renderDepth);
	}
	
	public void addControl(String key, String action){
		keys.put(key, action);
	}
	
	public void set(String key, String action){
		keys.put(key, action);
	}
	
	public void removeControl(String key){
		keys.remove(key);
	}
	
}
