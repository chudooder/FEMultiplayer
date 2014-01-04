package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import chu.engine.anim.AudioPlayer;
import net.fe.overworldStage.Menu;
import net.fe.overworldStage.MenuContext;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;

public class UnitMoved extends MenuContext<String> {
	private Unit unit;
	private Zone zone;
	private boolean fromTrade;

	public UnitMoved(OverworldStage stage, OverworldContext prev, Unit u,
			boolean fromTrade) {
		super(stage, prev, new Menu<String>(0, 0));
		unit = u;
		for (String cmd : getCommands(unit)) {
			menu.addItem(cmd);
		}
		this.fromTrade = fromTrade;
	}

	public void startContext() {
		super.startContext();

		updateZones();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		
		stage.setMovX(unit.getXCoord() - unit.getOrigX());
		stage.setMovY(unit.getYCoord() - unit.getOrigY());
	}
	
	public void cleanUp(){
		super.cleanUp();
		stage.removeEntity(zone);
	}
	
	@Override
	public void onSelect(String selectedItem) {
		// TODO Finish this
		stage.setMenu(null);
		AudioPlayer.playAudio("select", 1, 1);
		if (selectedItem.equals("Wait")) {
			stage.addCmd("Wait");
			stage.send();
			unit.moved();
			stage.reset();	
		} else if (selectedItem.equals("Attack")) {
			new AttackTarget(stage, this, zone, unit).startContext();
		} else if (selectedItem.equals("Heal")){
			new HealTarget(stage, this, zone, unit).startContext();
		} else if (selectedItem.equals("Item")){	
			new ItemCmd(stage, this, unit).startContext();
		} else if (selectedItem.equals("Trade")){
			new TradeTarget(stage, this, zone, unit).startContext();
		}
	}

	public void onChange() {
		updateZones();
	}
	

	@Override
	public void onCancel() {
		if (fromTrade){
			return; // You can't cancel this.
		}
		super.onCancel();
	}

	public void updateZones() {
		stage.removeEntity(zone);
		if (menu.getSelection().equals("Attack")) {
			zone = new Zone(grid.getRange(
					new Node(unit.getXCoord(), unit.getYCoord()),
					unit.getTotalWepRange(false)), Zone.ATTACK_DARK);
			stage.addEntity(zone);
		} else if (menu.getSelection().equals("Heal")) {
			zone = new Zone(grid.getRange(
					new Node(unit.getXCoord(), unit.getYCoord()),
					unit.getTotalWepRange(true)), Zone.HEAL_DARK);
			stage.addEntity(zone);
		} else if (menu.getSelection().equals("Trade")) {
			zone = new Zone(grid.getRange(
					new Node(unit.getXCoord(), unit.getYCoord()), 1),
					Zone.MOVE_DARK);
			stage.addEntity(zone);
		}
	}

	public List<String> getCommands(Unit u) {
		// TODO Rescue
		List<String> list = new ArrayList<String>();
		boolean attack = false;
		Set<Node> range = grid.getRange(new Node(u.getXCoord(), u.getYCoord()),
				unit.getTotalWepRange(false));
		for (Node n : range) {
			Unit p = grid.getUnit(n.x, n.y);
			if (p != null && !stage.getPlayer().getParty().isAlly(p.getParty())) {
				attack = true;
				break;
			}
		}
		if (attack)
			list.add("Attack");

		boolean heal = false;
		range = grid.getRange(new Node(u.getXCoord(), u.getYCoord()),
				unit.getTotalWepRange(true));
		for (Node n : range) {
			Unit p = grid.getUnit(n.x, n.y);
			if (p != null && stage.getPlayer().getParty().isAlly(p.getParty())
					&& p.getHp() != p.get("HP")) {
				heal = true;
				break;
			}
		}
		if (heal)
			list.add("Heal");

		boolean trade = false;
		range = grid.getRange(new Node(u.getXCoord(), u.getYCoord()), 1);
		for (Node n : range) {
			Unit p = grid.getUnit(n.x, n.y);
			if (p != null && stage.getPlayer().getParty().isAlly(p.getParty())) {
				trade = true;
				break;
			}
		}
		if (fromTrade)
			trade = false;
		if (trade)
			list.add("Trade");

		list.add("Item");
		list.add("Wait");

		return list;
	}

	@Override
	public void onLeft() {
		// Nothing
	}

	@Override
	public void onRight() {
		// Nothing
	}

}
