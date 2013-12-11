package net.fe.overworldStage;
import chu.engine.Game;
import chu.engine.GriddedEntity;
import chu.engine.Resources;
import chu.engine.anim.Animation;

public class Cursor extends GriddedEntity{
	private float time;
	public Cursor(int xx, int yy) {
		super(xx, yy);
		sprite.addAnimation("default", new Animation(Resources.getTexture("cursor"),
				24, 24, 16, 16, 4, 4, 50));
		renderDepth = OverworldStage.MENU_DEPTH;
	}
	
	public void onStep(){
		sprite.update();
		time+= Game.getDeltaSeconds();
		if(time >=1.5){
			time -=1.5;
		}
		
	}
	
	public void render(){
		if(((OverworldStage) stage).hasControl())
			sprite.render(x, y, renderDepth);
	}

	public void setXCoord(int xcoord) {
		this.xcoord = xcoord;
	}

	public void setYCoord(int ycoord) {
		this.ycoord = ycoord;
	}

}
