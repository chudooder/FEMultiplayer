package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;

public class SkillIndicator extends Entity{
	private String message;
	private float elapsedTime;
	private boolean extended;
	private int vx;
	private int x0;
	private int xt;
	private boolean left;
	
	public static final int MSG_TIME = 1;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 12;
	
	public SkillIndicator(String message, boolean left, int position){
		super(0,29 + position*(HEIGHT+2));
		this.message = message;
		elapsedTime = 0;
		extended = false;
		vx = left? 800: -800;
		x = left? -WIDTH: FightStage.CENTRAL_AXIS*2;
		xt = left? 0: FightStage.CENTRAL_AXIS*2-WIDTH;
		x0 = (int) x;
		this.left = left;
		
		renderDepth = FightStage.HUD_DEPTH;
	}
	
	public void onStep(){
		if(extended){
			elapsedTime += Game.getDeltaSeconds();
			if(elapsedTime > MSG_TIME){
				extended = false;
			}
		} else if(elapsedTime == 0){
			x += vx * Game.getDeltaSeconds();
			if(Math.abs(x - x0)+3 >= WIDTH){
				extended = true;
				x = xt;
			}
		} else {
			x -= vx * Game.getDeltaSeconds();
			if(Math.abs(x - x0) <= 0){
				destroy();
			}
		}
	}
	
	public void render(){
		if(left){
			Renderer.drawRectangle(x-10, y, x+WIDTH, y + HEIGHT, 0, FightStage.NEUTRAL);
		} else {
			Renderer.drawRectangle(x, y, x+WIDTH+10, y + HEIGHT, 0, FightStage.NEUTRAL);
		}
		Renderer.drawString("msg_text", message, x+4, y+2, 0.0f);
	}
}
