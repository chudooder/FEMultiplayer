package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.fe.FEMultiplayer;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

public class UnitMoved extends MenuContext {
	private Unit unit;
	private Zone zone;

	public UnitMoved(OverworldStage stage, OverworldContext prev, Unit u) {
		super(stage, prev, new Menu(0, 0));
		unit = u;
		for (String cmd : getCommands(unit)) {
			menu.addItem(cmd);
		}
	}

	public void startContext() {
		super.startContext();
		
		updateZones();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
	}

	public List<String> getCommands(Unit u) {
		// TODO Rescue
		List<String> list = new ArrayList<String>();
		boolean attack = false;
		Set<Node> range = grid.getRange(new Node(u.getXCoord(), u.getYCoord()),
				unit.getTotalWepRange(false));
		for(Node n: range){
			Unit p = grid.getUnit(n.x, n.y);
			if(p != null && !stage.getPlayer().getParty().isAlly(p.getParty())){
				attack = true;
				break;
			}
		}
		if(attack)
			list.add("Attack");
		
		boolean heal = false;
		range = grid.getRange(new Node(u.getXCoord(), u.getYCoord()),
				unit.getTotalWepRange(true));
		for(Node n: range){
			Unit p = grid.getUnit(n.x, n.y);
			if(p != null && stage.getPlayer().getParty().isAlly(p.getParty())){
				heal = true;
				break;
			}
		}
		if(heal)
			list.add("Heal");
		
		list.add("Item");
		list.add("Wait");
		
		return list;
	}

	@Override
	public void onSelect(String selectedItem) {
		// TODO
		stage.setMenu(null);
		System.out.println("Selected " + selectedItem);
		if (selectedItem.equals("Wait")) {
			unit.moved();
			stage.returnToNeutral();
		} else if (selectedItem.equals("Attack")) {
			// TODO Select Target
			new SelectAttackTarget(stage, this, zone, unit, false);
		} else if (selectedItem.equals("Heal")) {

		}
	}

	public void onChange() {
		updateZones();
	}

	public void updateZones() {
		stage.removeEntity(zone);
		if (menu.getSelection().equals("Attack")) {
			zone = new Zone(grid.getRange(new Node(unit.getXCoord(), unit.getYCoord()),
					unit.getTotalWepRange(false)), Zone.ATTACK_DARK);
			stage.addEntity(zone);
		} else if (menu.getSelection().equals("Heal")) {
			zone = new Zone(grid.getRange(new Node(unit.getXCoord(), unit.getYCoord()),
					unit.getTotalWepRange(true)), Zone.HEAL_DARK);
			stage.addEntity(zone);
		}
	}

	@Override
	public void onCancel() {

		stage.setMenu(null);
		stage.removeEntity(zone);
		prev.startContext();
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
