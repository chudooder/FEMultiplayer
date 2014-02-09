package net.fe.unit;

import net.fe.FEResources;
import net.fe.Party;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.anim.Renderer;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;

public class UnitIcon extends Entity {
	private Color c;
	private Unit u;
	private boolean greyscale;
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
	}
	public void render(){
		if(FEResources.hasTexture(u.functionalClassName().toLowerCase() + "_map_idle")){
			Transform t = new Transform();
			if(greyscale){
				sprite.render(x+1, y+1, renderDepth, t, new ShaderArgs("greyscale"));
			} else if(c.equals(Party.TEAM_RED)) {
				sprite.render(x+1, y+1, renderDepth, t, new ShaderArgs("paletteSwap"));
			} else {
				sprite.render(x+1, y+1, renderDepth, t, new ShaderArgs("default"));
			}
		}else {
			Color c = this.c;
			if(greyscale) c = Color.gray;
			Renderer.drawRectangle(x + 1, y + 1, x + 14,
					y + 14, renderDepth, c);
			Renderer.drawString("default_med",
					u.name.charAt(0) + "" + u.name.charAt(1), x + 2, y + 1,
					renderDepth);
			
		}
	}
	public boolean isGreyscale() {
		return greyscale;
	}
	public void setGreyscale(boolean greyscale) {
		this.greyscale = greyscale;
	}
	
}
