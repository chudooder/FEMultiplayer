package net.fe.overworldStage;

import java.util.Iterator;

import chu.engine.anim.Renderer;
import net.fe.unit.*;

public class InventoryMenu extends Menu<ItemDisplay> {
	private Unit unit;
	public InventoryMenu(Unit u, float x, float y) {
		super(x, y);
		width = 98;
		unit = u;
		height = 17;
	}
	
	public InventoryMenu(Unit u){
		this(u,0,0);
	}
	public void onStep(){
		items.clear();
		Iterator<Item> inv = unit.getInventory().iterator();
		for(int i = 0; i < 4; i++){
			if(inv.hasNext()){
				items.add(new ItemDisplay(0,0, inv.next()));
			} else {
				items.add(null);
			}
		}
	}
	public void renderItem(ItemDisplay w, int offsetY){
		if(w == null) return;
		w.x = this.x;
		w.y = this.y + offsetY;
		w.render();
			int uses = w.getItem().getUses();
			int offX = uses<10? 7: 0;
			offX+=80;
			Renderer.drawString("default_med", 
				uses + "",
				x+offX, y + offsetY+2, renderDepth);
	}
}
