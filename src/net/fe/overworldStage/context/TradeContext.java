package net.fe.overworldStage.context;

import java.util.List;

import chu.engine.anim.AudioPlayer;
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
		trader = new TradeMenu(u1, 72, 130, false);
		tradee = new TradeMenu(u2, 199, 130, true);
		curr = trader;
		tradee.clearSelection();
		marked = -1;
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
			AudioPlayer.playAudio("select", 1, 1);
			marked = getIndex();
			curr.mark(curr.getSelectedIndex());
			curr.down();
			
			switchMenu();
			curr.setSelection(0);
		} else {
			//Trade
			AudioPlayer.playAudio("cancel", 1, 1);
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
				AudioPlayer.playAudio("cancel", 1, 1);
				cursor.setXCoord(u1.getXCoord());
				cursor.setYCoord(u1.getYCoord());
				new UnitMoved(stage, this, u1, true, false).startContext();
			}
		} else {
			AudioPlayer.playAudio("cancel", 1, 1);
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
		AudioPlayer.playAudio("cursor2", 1, 1);
	}

	@Override
	public void onDown() {
		curr.down();
		AudioPlayer.playAudio("cursor2", 1, 1);
	}

	@Override
	public void onLeft() {
		AudioPlayer.playAudio("cursor2", 1, 1);
		int i = curr.getSelectedIndex();
		switchMenu();
		curr.setSelection(i);
	}

	@Override
	public void onRight() {
		AudioPlayer.playAudio("cursor2", 1, 1);
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
