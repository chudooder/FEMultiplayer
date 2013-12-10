package net.fe.unit;


import chu.engine.Entity;
import chu.engine.Resources;
import chu.engine.anim.Renderer;

public class ItemDisplay extends Entity{
	private Item item;
	public ItemDisplay(int x, int y, Item i){
		super(x,y);
		renderDepth = 0.05f;
		item = i;
	}
	public void render(){
		int row = item.id/8;
		int col = item.id%8;
		Renderer.render(Resources.getTexture("gui_weaponIcon"), 
				col/8.0f, row/9.0f, (col+1)/8.0f, (row+1)/9.0f,
				x-1, y, x+16, y+17, renderDepth);
//		Resources.getBitmapFont("default_med").render("Fimbulvetr", x+16, y+3, 0.0f);
		Resources.getBitmapFont("default_med").render(item.name, x+16, y+3, renderDepth);
	}
	
	public Item getItem(){
		return item;
	}
}
