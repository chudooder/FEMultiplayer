package net.fe.overworldStage;
import net.fe.FEResources;
import chu.engine.Game;
import chu.engine.GriddedEntity;
import chu.engine.anim.Animation;

public class Cursor extends GriddedEntity  implements DoNotDestroy{
	private float time;
	private boolean on;
	private float rx;
	private float ry;
	
	private static final float SPEED = 1000f;
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
		if(((ClientOverworldStage) stage).hasControl() && on) {
			sprite.render(x, y, renderDepth);
		}
	}

	public void setXCoord(int xcoord) {
		this.xcoord = xcoord;
	}

	public void setYCoord(int ycoord) {
		this.ycoord = ycoord;
	}
	
	public void off(){
		on = false;
	}
	
	public void on(){
		on = true;
	}

}
