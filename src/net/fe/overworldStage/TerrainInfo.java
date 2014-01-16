package net.fe.overworldStage;

import static net.fe.fightStage.FightStage.*;
import net.fe.FEResources;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;

public class TerrainInfo extends Entity implements DoNotDestroy{

	public TerrainInfo(Cursor c) {
		super(0, Game.getWindowHeight()-84);
		renderDepth = 0.8f;
	}
	
	public void render() {
		Terrain terrain = ((ClientOverworldStage)stage).getHoveredTerrain();
		
		//Main box
		Renderer.drawRectangle(x, y, x+50, y+84, renderDepth, BORDER_DARK);
		Renderer.drawRectangle(x+1, y+1, x+49, y+83, renderDepth, BORDER_LIGHT);
		Renderer.drawRectangle(x+2, y+2, x+48, y+82, renderDepth, 
				NEUTRAL);
		//Terrain name ribbon
		Renderer.drawRectangle(x+3, y+3, x+47, y+30, renderDepth, 
				NEUTRAL.darker(0.5f));
		BitmapFont def = FEResources.getBitmapFont("default_med");
		int width = def.getStringWidth(terrain.toString());
		Renderer.drawString("default_med", terrain.toString(), x+25-width/2, y+10, renderDepth);
		//Separator
		Renderer.render(FEResources.getTexture("dragon_separator"), 0, 0, 1, 1,
				x+7, y+32, x+45, y+41, renderDepth);
		//Terrain stats
		Renderer.drawString("default_med", "AVO", x+5, y+44, renderDepth);
		Renderer.drawString("default_med", terrain.avoidBonus, 
				x+46-def.getStringWidth(terrain.avoidBonus+""), y+44, renderDepth);
		Renderer.drawString("default_med", "DEF", x+5, y+57, renderDepth);
		Renderer.drawString("default_med", terrain.defenseBonus, 
				x+46-def.getStringWidth(terrain.defenseBonus+""), y+57, renderDepth);
		Renderer.drawString("default_med", "HP", x+5, y+70, renderDepth);
		Renderer.drawString("default_med", terrain.healthBonus, 
				x+46-def.getStringWidth(terrain.healthBonus+""), y+70, renderDepth);
	}

}
