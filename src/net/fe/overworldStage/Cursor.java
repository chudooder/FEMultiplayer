package net.fe.overworldStage;
import net.fe.FEResources;
import chu.engine.GriddedEntity;
import chu.engine.anim.Animation;

public class Cursor extends GriddedEntity  implements DoNotDestroy{
	private boolean on;
	private static final int border = 64;
	public Cursor(int xx, int yy) {
		super(xx, yy);
		sprite.addAnimation("default", new Animation(FEResources.getTexture("cursor"),
				24, 24, 16, 16, 4, 4, 0.05f));
		renderDepth = ClientOverworldStage.CURSOR_DEPTH;
		on = true;
	}
	
	public void onStep(){
		sprite.update();
		ClientOverworldStage s = ((ClientOverworldStage) stage);
		s.unitInfo.setUnit(s.getHoveredUnit());
	}
	
	public void render(){
		ClientOverworldStage c = (ClientOverworldStage)stage;
		if(c.hasControl() && on) {
			if(x - c.camX > 367 || y - c.camY > 239) return; 
			sprite.render(x - c.camX, y - c.camY, renderDepth);
		}
	}

	public void setXCoord(int xcoord) {
		this.xcoord = xcoord;
		updateCamera();
	}

	public void setYCoord(int ycoord) {
		this.ycoord = ycoord;
		updateCamera();
	}
	
	public void updateCamera() {
		ClientOverworldStage c = (ClientOverworldStage)stage;
		int rX = xcoord*16 - c.camX;
		if(rX < border) {
			c.camX = Math.max(0, xcoord*16 - border);
		} else if(rX > 368 - border) {
			c.camX = Math.min(c.camMaxX, xcoord*16 - (368 - border));
		}
		int rY = ycoord*16 - c.camY;
		if(rY < border) {
			c.camY = Math.max(0, ycoord*16 - border);
		} else if(rY > 240 - border) {
			c.camY = Math.min(c.camMaxY, ycoord*16 - (240 - border));
		}
	}
	
	public void off(){
		on = false;
	}
	
	public void on(){
		on = true;
	}

}
