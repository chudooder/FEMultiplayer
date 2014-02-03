package net.fe.overworldStage;

import org.newdawn.slick.Color;

import net.fe.FEResources;
import net.fe.Player;
import net.fe.fightStage.FightStage;
import net.fe.unit.Unit;
import chu.engine.Entity;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;

public class ObjectiveInfo extends Entity implements DoNotDestroy{
	//CONFIG
	public static final int WIDTH = 96;
	public static final int HEIGHT = 70;
	public ObjectiveInfo() {
		super(480-WIDTH - 2, 2);
		renderDepth = ClientOverworldStage.MENU_DEPTH;
	}
	public void render(){
		OverworldStage s = ((OverworldStage) stage);
		String objective = s.getObjective().getDescription();
		String turn = "Turn " + s.getTurnCount();
		Renderer.drawBorderedRectangle(x, y, x+ WIDTH, y + HEIGHT, renderDepth,
				FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
		Renderer.drawString("default_med", objective, x+4, y+4, renderDepth);
		Renderer.drawString("default_med", turn, x+4, y+20, renderDepth);
		float ystart = y + 36;
		for(Player p: s.getPlayers()){
			Transform t = new Transform();
			Color c = p.getParty().getColor().brighter();
			c.r = Math.max(c.r, 0.5f);
			c.g = Math.max(c.g, 0.5f);
			c.b = Math.max(c.b, 0.5f);
			t.setColor(c);
			Renderer.drawString("default_med", p.getName(), x + 8, ystart, renderDepth, t);
			int unitCount = 0;
			for(Unit u: p.getParty()){
				if(u.getHp() > 0) unitCount++;
			}
			String units = unitCount + "";
			int unitsWidth = FEResources.getBitmapFont("default_med").getStringWidth(units);
			Renderer.drawString("default_med", units, 
					x + WIDTH - unitsWidth - 8, ystart, renderDepth, t);
			ystart += 16;
		}
	}
}
