package net.fe.overworldStage;
import chu.engine.Game;
import chu.engine.GriddedEntity;
import chu.engine.Resources;
import chu.engine.anim.Animation;

public class Cursor extends GriddedEntity{
	private float time;
	private boolean on;
	private float rx;
	private float ry;
	
	private static final float SPEED = 1000f;
	public Cursor(int xx, int yy) {
		super(xx, yy);
		sprite.addAnimation("default", new Animation(Resources.getTexture("cursor"),
				24, 24, 16, 16, 4, 4, 0.05f));
		renderDepth = OverworldStage.CURSOR_DEPTH;
		on = true;
	}
//	
//	public void onStep(){
//		sprite.update();
//		float delta = Game.getDeltaSeconds();
//		//TODO: What does this variable do
//		time+= delta;
//		if(time >=1.5){
//			time -=1.5;
//		}
//		if(rx != x) {
//			float signum = Math.signum(x-rx);
//			rx += signum*SPEED*delta;
//			if(Math.signum(x-rx) != signum) {
//				rx = x;
//			}
//		}
//		if(ry != y) {
//			float signum = Math.signum(y-ry);
//			ry += signum*SPEED*delta;
//			if(Math.signum(y-ry) != signum) {
//				ry = y;
//			}
//		}
//	}
	
	public void render(){
		if(((OverworldStage) stage).hasControl() && on) {
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
