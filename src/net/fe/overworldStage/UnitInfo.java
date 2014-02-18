package net.fe.overworldStage;

import static net.fe.fightStage.FightStage.BORDER_DARK;
import static net.fe.fightStage.FightStage.BORDER_LIGHT;
import static net.fe.fightStage.FightStage.NEUTRAL;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.fe.FEResources;
import net.fe.unit.Item;
import net.fe.unit.ItemDisplay;
import net.fe.unit.Unit;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;

public class UnitInfo extends Entity implements DoNotDestroy{
	private Unit unit;
	private Texture mugshot;
	private Texture dragons;
	private ItemDisplay[] items;
	public UnitInfo() {
		super(50, Game.getWindowHeight()-80);
		renderDepth = 0.8f;
		dragons = FEResources.getTexture("dragon_separator");
	}
	
	public UnitInfo(float x, float y){
		super(x, y);
		renderDepth = 0.8f;
	}
	
	public Unit getUnit(){
		return unit;
	}
	
	public void setUnit(Unit u){
		if(u != null && unit != u) {
			mugshot = FEResources.getTexture(u.name.toLowerCase()+"_mugshot");
			Iterator<Item> inv = u.getInventory().iterator();
			items = new ItemDisplay[4];
			int y0 = 18;
			for (int i = 0; i < 4; i++) {
				if (inv.hasNext()) {
					Item it = inv.next();
					items[i] = new ItemDisplay(x + 210, y + y0, it, u.getWeapon() == it);
				}
				y0 += 14;
			}
		}
		unit = u;
	}
	
	public void render(){
		Unit u = unit;
		if(u == null) return;
		if(u.isDying()) return;
		
		//Main Box
		Renderer.drawRectangle(x, y, x+320, y+80, renderDepth, BORDER_DARK);
		Renderer.drawRectangle(x+1, y+1, x+319, y+79, renderDepth, BORDER_LIGHT);
		Renderer.drawRectangle(x+2, y+2, x+318, y+78, renderDepth, 
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
		Renderer.drawRectangle(x+4, y+4, x+88, y+78, renderDepth, BORDER_DARK);
		Renderer.drawRectangle(x+5, y+5, x+87, y+77, renderDepth, BORDER_LIGHT);
		Renderer.drawRectangle(x+6, y+6, x+86, y+76, renderDepth, NEUTRAL.darker(0.5f));
		Renderer.addClip(x+6, y+6, 80, 72, false);
		Renderer.render(mugshot, 0, 0, 1, 1, x+46-mugshot.getImageWidth()/2, 
				y+76-mugshot.getImageHeight(), 
				x+46+mugshot.getImageWidth()/2, y+76, renderDepth-0.01f);
		
		//Stats
		
		int y0 = 22;
		for(String stat: Arrays.asList("Str","Mag","Skl","Spd")){
			Renderer.drawString("default_med", stat, x+92, y+y0, renderDepth);
			int statN = u.getBase(stat);
			width = FEResources.getBitmapFont("default_med").getStringWidth(statN+"");
			Renderer.drawString("default_med", statN, 
					x+122-(width-1)/2, y+y0, renderDepth);
			y0+=14;
		}
		
		y0 = 22;
		for(String stat: Arrays.asList("Lck","Def","Res","Mov")){
			Renderer.drawString("default_med", stat, x+152, y+y0, renderDepth);
			int statN = u.getBase(stat);
			width = FEResources.getBitmapFont("default_med").getStringWidth(statN+"");
			Renderer.drawString("default_med", statN, 
					x+182-(width-1)/2, y+y0, renderDepth);
			y0+=14;
		}
		
		//Inventory
		Renderer.drawRectangle(x+208, y+20, x+318, y+78, renderDepth,
				NEUTRAL.darker(0.5f));
		y0 = 22;
		for (int i = 0; i < 4; i++) {
			if(items[i] != null){
				items[i].render();
				String uses = items[i].getItem().getUses() + "";
				width = FEResources.getBitmapFont("default_med").getStringWidth(uses);
				Renderer.drawString("default_med", uses, x+314-width, y+y0, renderDepth);
			}
			y0+=14;
		}

		// Rescued Unit
		Unit rescued = u.rescuedUnit();
		if (rescued != null) {
			// Main box
			Renderer.drawRectangle(x + 320, y, x + 110 + 320, y + 80,
					renderDepth, BORDER_DARK);
			Renderer.drawRectangle(x + 321, y + 1, x + 109 + 320, y + 79,
					renderDepth, BORDER_LIGHT);
			Renderer.drawRectangle(x + 322, y + 2, x + 108 + 320, y + 78,
					renderDepth, NEUTRAL);
			// Terrain name ribbon
			Renderer.drawRectangle(x + 323, y + 3, x + 107 + 320, y + 77,
					renderDepth, NEUTRAL.darker(0.5f));
			BitmapFont def = FEResources.getBitmapFont("default_med");
			width = def.getStringWidth("Rescued");
			Renderer.drawString("default_med", "RESCUED", x + 320 + 55 - width / 2,
					y + 10, renderDepth);
			// Separator
			Renderer.render(dragons, 0, 0,
					1, 1, x + 37 + 320, y + 32, x + 75 + 320, y + 41, renderDepth);

			// Info
			Renderer.drawString("default_med",
					rescued.name + "  Lv" + rescued.get("Lvl"), x + 325, y + 47,
					renderDepth);
			Renderer.drawString("default_med",
					"HP " + rescued.getHp() + "/" + rescued.get("HP"), x + 325, y + 63,
					renderDepth);
		}
	}

}
