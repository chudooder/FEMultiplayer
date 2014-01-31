package net.fe.fightStage;

import net.fe.FEResources;
import net.fe.unit.Unit;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;

public class Healthbar extends Entity {
	private int totalHealth;
	private int currentHealth;
	private float displayedHealth;
	public boolean doneAnimating;
	private boolean doNotSet;

	public Healthbar(Unit u, boolean left) {
		super(0, 0);
		doneAnimating = true;
		if(left){
			x = FightStage.CENTRAL_AXIS - 91;
		} else {
			x = FightStage.CENTRAL_AXIS + 30;
		}
		if(u.get("HP") > 40){
			y = FightStage.FLOOR + 38;
		} else {
			y = FightStage.FLOOR + 42;
		}
		totalHealth = u.get("HP");
		currentHealth = u.getHp();
		displayedHealth = u.getHp();
		renderDepth = FightStage.HP_DEPTH;
	}

	public void render() {
		int offY = 0;
		int offX = 0;
		int width = FEResources.getBitmapFont("stat_numbers").getStringWidth((int)displayedHealth + "");
		if(totalHealth <= 40) {
			Renderer.drawString("stat_numbers", (int)displayedHealth + "", x-5-width, y-2, renderDepth);
		} else {
			Renderer.drawString("stat_numbers", (int)displayedHealth + "", x-5-width, y+2, renderDepth);
		}
		for (int hp = 1; hp <= totalHealth; hp++) {
			Texture t = FEResources
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
			doneAnimating = false;
			displayedHealth += Math.signum(currentHealth - displayedHealth)*15*Game.getDeltaSeconds();
		} else if(displayedHealth != currentHealth){
			displayedHealth = currentHealth;
			if(!doNotSet){
				doneAnimating = true;
			}
		}
	}
	
	public int getHp(){
		return currentHealth;
	}
	
	public void setHp(int hp){
		currentHealth = hp;
		doNotSet = false;
	}
	
	public void setHp(int hp, boolean stateShift){
		doNotSet = !stateShift;
		setHp(hp);
	}

}
