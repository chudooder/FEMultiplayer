package net.fe.overworldStage;

import net.fe.fightStage.FightStage;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Resources;
import chu.engine.anim.Renderer;

public abstract class Healthbar extends Entity {
	private int hp1;
	private float displayedHealth, totalHealth;
	private float time;
	public Healthbar(float x, float y, int hp0, int hp1, int hpT) {
		super(x, y);
		System.out.println(hp0 + "->" + hp1);
		this.hp1 = hp1;
		this.displayedHealth = hp0;
		this.totalHealth = hpT;
	}
	
	public void onStep(){
		if(Math.abs(displayedHealth-hp1) >= 1){
			displayedHealth += Math.signum(hp1 - displayedHealth)
					*30*Game.getDeltaSeconds();
		} else if(displayedHealth != hp1){
			displayedHealth = hp1;
			
		} else {
			time+= Game.getDeltaSeconds();
			if(time > 1)
				done();
		}
	}
	
	public abstract void done();
	
	public void render(){
		Renderer.drawRectangle(x-24, y-6, x+85, y+20, renderDepth, FightStage.BORDER_DARK);
		Renderer.drawRectangle(x-23, y-5, x+84, y+19, renderDepth, FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x-22, y-4, x+83, y+18, renderDepth, FightStage.NEUTRAL);
		int offY = 0;
		int offX = 0;
		int width = Resources.getBitmapFont("stat_numbers").getStringWidth((int)displayedHealth + "");
		if(totalHealth <= 40) {
			Renderer.drawString("stat_numbers", (int)displayedHealth + "", x-5-width, y-2, renderDepth);
		} else {
			Renderer.drawString("stat_numbers", (int)displayedHealth + "", x-5-width, y+2, renderDepth);
		}
		for (int hp = 1; hp <= totalHealth; hp++) {
			Texture t = Resources
					.getTexture(hp <= displayedHealth ? "gui_tickFilled"
							: "gui_tickEmpty");
			Renderer.render(t, 0, 0, 1, 1, x + offX, y + offY, x + offX + 2, y
					+ offY + 6, renderDepth);
			
			if(hp == 40){
				offY = 8;
				offX = 0;
			} else {
				offX +=2;
			}
		}
	}

}
