package net.fe.overworldStage;

import static net.fe.fightStage.FightStage.*;

import org.newdawn.slick.Color;

import net.fe.unit.Unit;
import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class UnitInfo extends Entity{
	public UnitInfo(Cursor c) {
		//TODO Positioning
		super(0, 150);
		renderDepth = OverworldStage.MENU_DEPTH;
	}
	
	public void render(){
		Unit u = ((OverworldStage) stage).getHoveredUnit();
		if(u == null) return;
		
		//Main Box
		Renderer.drawRectangle(x, y, x+330, y+84, renderDepth, BORDER_DARK);
		Renderer.drawRectangle(x+1, y+1, x+329, y+83, renderDepth, BORDER_LIGHT);
		Renderer.drawRectangle(x+2, y+2, x+328, y+82, renderDepth, 
				NEUTRAL);
		
		//Ribbon
		Renderer.drawRectangle(x+2, y+2, x+328, y+20, renderDepth, BORDER_DARK);
		Renderer.drawRectangle(x+2, y+2, x+328, y+19, renderDepth, BORDER_LIGHT);
		Renderer.drawRectangle(x+2, y+2, x+328, y+18, renderDepth, u.getPartyColor());
		Renderer.drawString("default_med", u.name, x+92, y+4, renderDepth);
		
		//Mugshot
		Renderer.drawRectangle(x+4, y+4, x+88, y+80, renderDepth, BORDER_DARK);
		Renderer.drawRectangle(x+5, y+5, x+87, y+79, renderDepth, BORDER_LIGHT);
		Renderer.drawRectangle(x+6, y+6, x+86, y+78, renderDepth, Color.gray);
		
		//Stats
		Renderer.drawString("default_med", "Str", x+92, y+20, renderDepth);
		Renderer.drawString("default_med", "Mag", x+92, y+35, renderDepth);
		Renderer.drawString("default_med", "Skl", x+92, y+50, renderDepth);
		Renderer.drawString("default_med", "Spd", x+92, y+65, renderDepth);
		
		Renderer.drawString("default_med", u.get("Str")+"", x+100, y+20, renderDepth);
		
		
		//Inventory
		Renderer.drawRectangle(x+208, y+20, x+308, y+82, renderDepth, 
				NEUTRAL.darker(0.5f));
		

	}

}
