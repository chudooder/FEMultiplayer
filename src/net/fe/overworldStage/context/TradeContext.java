package net.fe.overworldStage.context;

import java.util.List;

import net.fe.overworldStage.*;
import net.fe.unit.*;

public class TradeContext extends OverworldContext {
	private Unit u1, u2;
	private Menu<ItemDisplay> trader, tradee, curr;
	private boolean traded;
	private int marked;
	public TradeContext(ClientOverworldStage s, OverworldContext prevContext, Unit u1, Unit u2) {
		super(s, prevContext);
		this.u1 = u1;
		this.u2 = u2;
		trader = new InventoryMenu(u1);
		tradee = new InventoryMenu(u2);
		curr = trader;
		tradee.clearSelection();
		marked = -1;
		
		trader.x = 225;
		trader.y = 0;
		tradee.x = 341;
		tradee.y = 0;
	}
	
	public void startContext(){
		super.startContext();
		stage.addEntity(trader);
		stage.addEntity(tradee);
		u1.unequip();
		u2.unequip();
	}
	
	public void cleanUp(){
		super.cleanUp();
		stage.removeEntity(trader);
		stage.removeEntity(tradee);
		u1.reEquip();
		u2.reEquip();
		
	}

	@Override
	public void onSelect() {
		if(marked == -1){
			marked = getIndex();
			curr.mark(curr.getSelectedIndex());
			curr.down();
			
			switchMenu();
			curr.setSelection(0);
		} else {
			//Trade
			
			if(swap(marked, getIndex())) traded = true;
			marked = -1;
			trader.unmark();
			tradee.unmark();
		}
	}

	public void onCancel() {
		if(marked == -1){
			if(!traded){
				super.onCancel();
			} else {
				cursor.setXCoord(u1.getXCoord());
				cursor.setYCoord(u1.getYCoord());
				new UnitMoved(stage, this, u1, true).startContext();
			}
		} else {
			Menu<ItemDisplay> m1, m2;
			if(marked < 4){
				m1 = trader;
				m2 = tradee;
			} else {
				m2 = trader;
				m1 = tradee;
			}
			int i = m1.getMarkedIndex();
			m1.unmark();
			m1.setSelection(i);
			curr = m1;
			m1.restoreSelection();
			m2.clearSelection();
			
			marked = -1;
			
		}
		
	}

	@Override
	public void onUp() {
		curr.up();

	}

	@Override
	public void onDown() {
		curr.down();
	}

	@Override
	public void onLeft() {
		int i = curr.getSelectedIndex();
		switchMenu();
		curr.setSelection(i);
	}

	@Override
	public void onRight() {
		int i = curr.getSelectedIndex();
		switchMenu();
		curr.setSelection(i);
	}
	
	
	private int getIndex(){
		int i = 0;
		if(curr == tradee){
			i+=4;
		}
		i+= curr.getSelectedIndex();
		return i;
	}
	
	private void switchMenu(){
		
		if(curr == trader){
			trader.clearSelection();
			tradee.restoreSelection();
			curr = tradee;
		} else {
			tradee.clearSelection();
			trader.restoreSelection();
			curr = trader;
		}
		
	}
	
	
	private boolean swap(int i1, int i2){
		List<Item> inv1 = u1.getInventory();
		List<Item> inv2 = u2.getInventory();
		List<Item> from = i1 < 4? inv1:inv2;
		List<Item> to = i2 < 4? inv1:inv2;
		int fromIndex = i1%4;
		int toIndex = i2%4;
		
		stage.addCmd("TRADE");
		
		if(from == inv1)
			stage.addCmd(new UnitIdentifier(u1));
		else
			stage.addCmd(new UnitIdentifier(u2));
		stage.addCmd(fromIndex);
		
		if(to == inv1)
			stage.addCmd(new UnitIdentifier(u1));
		else
			stage.addCmd(new UnitIdentifier(u2));
		stage.addCmd(toIndex);
		
		//BEGIN Crazy list ops
		//Check for meaningless trade
		if(toIndex >= to.size() && fromIndex >= from.size()) return false;
		
		if(toIndex >= to.size()){
			to.add(from.remove(fromIndex));
		} else if (fromIndex >= from.size()){
			from.add(to.remove(toIndex));
		} else {
			Item temp = to.get(toIndex);
			to.set(toIndex, from.get(fromIndex));
			from.set(fromIndex, temp);
		}

		return true;
	}

}
