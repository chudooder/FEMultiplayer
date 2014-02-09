package net.fe.overworldStage;

import org.newdawn.slick.opengl.Texture;

import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;
import net.fe.FEResources;
import net.fe.fightStage.FightStage;
import net.fe.unit.Unit;

public class TradeMenu extends InventoryMenu {
	private Texture mug;
	private boolean flip;
	public TradeMenu(Unit u, float x, float y, boolean flip) {
		super(u,x,y);
		mug = FEResources.getTexture(u.name.toLowerCase()+"_mugshot");
		this.flip = flip;
	}
	public void render(){
		int imageW = mug.getImageWidth();
		int imageH = mug.getImageHeight();
		int menuW = this.getWidth();
			
		
		Transform t = new Transform();
		if(flip)
			t.flipHorizontal();
		Renderer.render(mug, 0, 0, 1, 1, 
				x+menuW/2-imageW/2, y-imageH, x+menuW/2+imageW/2, y, renderDepth, t);
		super.render();
	}
}
