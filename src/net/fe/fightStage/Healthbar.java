package net.fe.fightStage;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.anim.Renderer;

public class Healthbar extends Entity {
	private int totalHealth;
	private int currentHealth;
	private float displayedHealth;

	public Healthbar(int hpMax, int hp, boolean left) {
		super(0, 0);
		x = 20 + (left ? 0 : 100);
		y = hpMax > 40 ? 120 : 116;
		totalHealth = hpMax;
		currentHealth = hp;
		displayedHealth = hp;
	}

	public void render() {
		int offY = 0;
		int offX = 0;
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

	public void onStep() {
		if(Math.abs(displayedHealth-currentHealth) >= 1){
			displayedHealth += Math.signum(currentHealth - displayedHealth)/30;
		} else {
			displayedHealth = currentHealth;
		}
	}
	
	public int getHp(){
		return currentHealth;
	}
	
	public void setHp(int hp){
		currentHealth = hp;
	}

}
