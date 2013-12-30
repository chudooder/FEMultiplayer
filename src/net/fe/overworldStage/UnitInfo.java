package net.fe.overworldStage;

import static net.fe.fightStage.FightStage.*;

import java.util.Arrays;
import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import net.fe.FEResources;
import net.fe.unit.Item;
import net.fe.unit.ItemDisplay;
import net.fe.unit.Unit;
import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class UnitInfo extends Entity{
	public UnitInfo(Cursor c) {
		//TODO Positioning
		super(0, 160);
		renderDepth = 0.8f;
	}
	
	public void render(){
		Unit u = ((OverworldStage) stage).getHoveredUnit();
		if(u == null) return;
		if(u.isDying()) return;
		
		//Main Box
		Renderer.drawRectangle(x, y, x+320, y+84, renderDepth, BORDER_DARK);
		Renderer.drawRectangle(x+1, y+1, x+319, y+83, renderDepth, BORDER_LIGHT);
		Renderer.drawRectangle(x+2, y+2, x+318, y+82, renderDepth, 
				NEUTRAL);
		
		//Ribbon
		Renderer.drawRectangle(x+2, y+2, x+318, y+20, renderDepth, BORDER_DARK);
		Renderer.drawRectangle(x+2, y+2, x+318, y+19, renderDepth, BORDER_LIGHT);
		Renderer.drawRectangle(x+2, y+2, x+318, y+18, renderDepth, u.getPartyColor());
		Renderer.drawString("default_med", u.name + "   Lv" + u.get("Lvl"), 
				x+92, y+4, renderDepth);
		String hp = "HP " + u.getHp() + "/" + u.get("HP");
		int width = FEResources.getBitmapFont("default_med").getStringWidth(hp);
		Renderer.drawString("default_med", hp, x+316-width, y+4, renderDepth);
		
		//Mugshot
		Renderer.drawRectangle(x+4, y+4, x+88, y+80, renderDepth, BORDER_DARK);
		Renderer.drawRectangle(x+5, y+5, x+87, y+79, renderDepth, BORDER_LIGHT);
		Renderer.drawRectangle(x+6, y+6, x+86, y+78, renderDepth, NEUTRAL.darker(0.5f));
		Texture mugshot = FEResources.getTexture(u.name.toLowerCase()+"_mugshot");
		Renderer.addClip(x+6, y+6, 80, 72, false);
		Renderer.render(mugshot, 0, 0, 1, 1, x+46-mugshot.getImageWidth()/2, 
				y+78-mugshot.getImageHeight(), 
				x+46+mugshot.getImageWidth()/2, y+78, renderDepth-0.01f);
		
		//Stats
		
		int y0 = 22;
		for(String stat: Arrays.asList("Str","Mag","Skl","Spd")){
			Renderer.drawString("default_med", stat, x+92, y+y0, renderDepth);
			int statN = u.get(stat);
			width = FEResources.getBitmapFont("default_med").getStringWidth(statN+"");
			Renderer.drawString("default_med", statN, 
					x+122-(width-1)/2, y+y0, renderDepth);
			y0+=15;
		}
		
		y0 = 22;
		for(String stat: Arrays.asList("Lck","Def","Res","Mov")){
			Renderer.drawString("default_med", stat, x+152, y+y0, renderDepth);
			int statN = u.get(stat);
			width = FEResources.getBitmapFont("default_med").getStringWidth(statN+"");
			Renderer.drawString("default_med", statN, 
					x+182-(width-1)/2, y+y0, renderDepth);
			y0+=15;
		}
		
		//Inventory
		Renderer.drawRectangle(x+208, y+20, x+318, y+82, renderDepth, 
				NEUTRAL.darker(0.5f));
		Iterator<Item> inv = u.getInventory().iterator();
		y0 = 20;
		for(int i = 0; i < 4; i++){
			if(inv.hasNext()){
				Item it = inv.next();
				new ItemDisplay(x+210, y+ y0, it, u.getWeapon()==it).render();
				int uses = it.getUses();
				int offX = uses<10? 7: 0;
				offX+=90;
				Renderer.drawString("default_med", uses + "",
					x+210+offX, y+2 + y0, renderDepth);
			} 
			y0+=15;
		}
		

	}

}
