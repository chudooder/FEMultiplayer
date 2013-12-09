package net.fe.unit;


import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.anim.Renderer;

public class WeaponDisplay extends Entity{
	private Weapon weapon;
	public WeaponDisplay(int x, int y, Weapon w){
		super(x,y);
		renderDepth = 0;
		weapon = w;
	}
	public void render(){
		int row = weapon.id/8;
		int col = weapon.id%8;
		Renderer.render(Resources.getTexture("gui_weaponIcon"), 
				col/8.0f, row/9.0f, (col+1)/8.0f, (row+1)/9.0f,
				x-1, y, x+16, y+17, 1);
//		Resources.getBitmapFont("default_med").render("Fimbulvetr", x+16, y+3, 0.0f);
		Resources.getBitmapFont("default_med").render(weapon.name, x+16, y+3, 0.0f);
	}
}
