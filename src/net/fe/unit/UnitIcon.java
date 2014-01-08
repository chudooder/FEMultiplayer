package net.fe.unit;

import net.fe.FEResources;
import net.fe.Party;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;

public class UnitIcon extends Entity {
	private Color c;
	private Unit u;
	public UnitIcon(Unit u, float x, float y, float depth){
		super(x, y);
		this.u = u;
		if(u.getParty() != null)
			this.c = u.getPartyColor();
		else
			this.c = Party.TEAM_BLUE;
		sprite.addAnimation("IDLE", new MapAnimation(u.functionalClassName() + 
				"_map_idle", false));
		renderDepth = depth;
	}
	public void onStep(){
		super.onStep();
		sprite.update();
	}
	public void render(){
		if(FEResources.hasTexture(u.functionalClassName() + "_map_idle")){
			Transform t = new Transform();
			if(c.equals(Party.TEAM_RED)) {
				sprite.render(x+1, y+1, renderDepth, t, "paletteSwap");
			} else {
				sprite.render(x+1, y+1, renderDepth, t, "default");
			}
		}else {
			Renderer.drawRectangle(x + 1, y + 1, x + 14,
					y + 14, renderDepth, c);
			Renderer.drawString("default_med",
					u.name.charAt(0) + "" + u.name.charAt(1), x + 2, y + 1,
					renderDepth);
			
		}
	}
}
