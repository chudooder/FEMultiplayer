package net.fe.overworldStage;

import static net.fe.fightStage.FightStage.*;

import org.newdawn.slick.opengl.Texture;

import net.fe.FEResources;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;

public class TerrainInfo extends Entity implements DoNotDestroy{
	
	private static Texture dragons = FEResources.getTexture("dragon_separator");

	public TerrainInfo(Cursor c) {
		super(0, Game.getWindowHeight()-80);
		renderDepth = 0.8f;
	}
	
	public void render() {
		Terrain terrain = ((ClientOverworldStage)stage).getHoveredTerrain();
		
		//Main box
		Renderer.drawRectangle(x, y, x+50, y+80, renderDepth, BORDER_DARK);
		Renderer.drawRectangle(x+1, y+1, x+49, y+79, renderDepth, BORDER_LIGHT);
		Renderer.drawRectangle(x+2, y+2, x+48, y+78, renderDepth, 
				NEUTRAL);
		//Terrain name ribbon
		Renderer.drawRectangle(x+3, y+3, x+47, y+26, renderDepth, 
				NEUTRAL.darker(0.5f));
		BitmapFont def = FEResources.getBitmapFont("default_med");
		int width = def.getStringWidth(terrain.toString());
		Renderer.drawString("default_med", terrain.toString(), x+25-width/2, y+8, renderDepth);
		//Separator
		Renderer.render(dragons, 0, 0, 1, 1,
				x+7, y+28, x+45, y+37, renderDepth);
		//Terrain stats
		Renderer.drawString("default_med", "AVO", x+5, y+40, renderDepth);
		Renderer.drawString("default_med", terrain.getAvoidBonus(null), 
				x+46-def.getStringWidth(terrain.getAvoidBonus(null)+""), y+40, renderDepth);
		Renderer.drawString("default_med", "DEF", x+5, y+53, renderDepth);
		Renderer.drawString("default_med", terrain.getDefenseBonus(null), 
				x+46-def.getStringWidth(terrain.getDefenseBonus(null)+""), y+53, renderDepth);
		Renderer.drawString("default_med", "HP", x+5, y+66, renderDepth);
		Renderer.drawString("default_med", terrain.healthBonus, 
				x+46-def.getStringWidth(terrain.healthBonus+""), y+66, renderDepth);
	}

}
