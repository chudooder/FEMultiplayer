package net.fe.unit;

import chu.engine.Resources;
import chu.engine.anim.Animation;

public class MapAnimation extends Animation {
	private float ftime, mtime;
	public MapAnimation(String name, boolean walking){
		super(Resources.getTexture(name), 32, 48, 4, 4, 9, 17, 0);
		if(walking){
			ftime = 0.15f;
			mtime = 0.15f;
		} else {
			ftime = 0.5f;
			mtime = 0.08f;
		}
	}
	public void update(){
		if(getFrame() %2 == 0){
			speed = ftime;
		} else {
			speed = mtime;
		}
		super.update();
	}
}
