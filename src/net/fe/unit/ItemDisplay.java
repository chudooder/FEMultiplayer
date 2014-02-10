package net.fe.unit;


import net.fe.FEResources;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;

public class ItemDisplay extends Entity{
	private Item item;
	private boolean equip;
	public ItemDisplay(float f, float g, Item i, boolean equip){
		super(f,g);
		renderDepth = 0.05f;
		item = i;
		this.equip = equip;
	}
	
	public void render(){
		render(null, false, 0);
	}
	
	public void render(Transform t, boolean effective, float timer) {
		if(item == null) return;
		int row = item.id/8;
		int col = item.id%8;
		ShaderArgs args;
		if(effective) {
			float exp = (float) (Math.sin(timer)/2 + .5f);
			args = new ShaderArgs("lighten", exp);
		} else {
			args = new ShaderArgs();
		}
		Renderer.render(FEResources.getTexture("gui_weaponIcon"), 
				col/8.0f, row/9.0f, (col+1)/8.0f, (row+1)/9.0f,
				x-1, y, x+16, y+17, renderDepth, t, args);
		FEResources.getBitmapFont("default_med").render(item.name, x+16, y+3, renderDepth, t);
		if(equip){
			Renderer.render(FEResources.getTexture("e"), 
					0, 0, 1, 1,
					x+10, y+10, x+16, y+17, renderDepth, t);
		}
	}
	
	public Item getItem(){
		return item;
	}
}
