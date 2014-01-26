package net.fe.builderStage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import net.fe.FEResources;
import net.fe.fightStage.FightStage;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;
import chu.engine.menu.TextInputBox;

public class TeamNameInput extends TextInputBox {
	private static final Color FOCUSED = new Color(0x817b58);
	private static final Color CURSOR = new Color(0xeeeeee);
	
	public static final String EXT = "femt";
	
	private boolean save;
	
	public TeamNameInput(boolean save) {
		super(190, 160, 100, 20, "default_med");
		this.save = save;
		renderDepth = 0f;
	}
	
	public void render(){
		Renderer.drawBorderedRectangle(x-10, y-20, x+width+10, y+height +5, renderDepth,
				FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
		Renderer.drawString("default_med", "Team Name:", x, y-15, renderDepth);
		BitmapFont font = FEResources.getBitmapFont("default_med");
		Renderer.drawRectangle(x, y, x+width, y+height, renderDepth, FOCUSED);
		float linepos = x + font.getStringWidth(input.substring(0, cursorPos)) + 2;
		Renderer.drawRectangle(linepos, y+1, linepos+1, y+height-1, renderDepth-0.02f, CURSOR);
		Renderer.drawString("default_med", input.toString(), x+2, y+5, renderDepth-0.01f);
	}
	
	public void beginStep(){
		super.beginStep();
		List<KeyboardEvent> keys = Game.getKeys();
		for(KeyboardEvent ke : keys) {
			if(ke.state) {
				if(ke.key == Keyboard.KEY_RETURN) { 
					if(!new File("teams").exists()){
						new File("teams").mkdir();
					}
					if(save){
						save();
					} else {
						load();
					}
					destroy();
					((TeamBuilderStage) stage).setControl(true);
				}
			}
		}
	}
	
	public void save(){
		try {
			((TeamBuilderStage) stage).saveTeam(new ObjectOutputStream(
					new FileOutputStream(convertPath("teams/" + input.toString()))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load(){
		try {
			((TeamBuilderStage) stage).loadTeam(new ObjectInputStream(
					new FileInputStream(convertPath("teams/" + input.toString()))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String convertPath(String path){
		if(!path.endsWith(EXT)){
			return path+"."+EXT;
		} else {
			return path;
		}
	}
	
	public void setStage(TeamBuilderStage s){
		hasFocus = true;
		s.addEntity(this);
		s.setControl(false);
	}

}
