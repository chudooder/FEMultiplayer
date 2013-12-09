package net.fe.overworldStage.context;

import java.util.Arrays;
import java.util.List;

import net.fe.FEMultiplayer;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.overworldStage.*;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

public class UnitMoved extends MenuContext {
	private int origX, origY;
	private Unit unit;
	private Zone zone;

	public UnitMoved(OverworldStage stage, OverworldContext prev, Unit u,
			int origX, int origY) {
		super(stage, prev, new Menu(0, 0));

		this.origX = origX;
		this.origY = origY;
		unit = u;
		for (String cmd : getCommands(unit)) {
			menu.addItem(cmd);
		}
		// TODO Auto-generated constructor stub
	}

	public void startContext() {
		super.startContext();
		
		updateZones();
		cursor.xcoord = unit.xcoord;
		cursor.ycoord = unit.ycoord;
	}

	public List<String> getCommands(Unit u) {
		// TODO
		return Arrays.asList("Attack", "Heal", "Item", "Wait");
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
			new SelectTarget(stage, this, zone, unit, false) {
				@Override
				public void updateCursor() {
					super.updateCursor();
					unit.equipFirstWeapon(Grid.getDistance(cursor.xcoord,
							cursor.ycoord, unit.xcoord, unit.ycoord));
				}

				@Override
				public void unitSelected(Unit u) {
					System.out.println("Selected " + u.name);
					unit.moved();
					stage.returnToNeutral();
					CombatCalculator calc = new CombatCalculator(
							new UnitIdentifier(unit), new UnitIdentifier(
									getHoveredUnit()));
					FEMultiplayer.setCurrentStage(new FightStage(
							new UnitIdentifier(unit), new UnitIdentifier(
									getHoveredUnit()), calc.getAttackQueue()));

				}
			}.startContext();
		} else if (selectedItem.equals("Heal")) {

		}
	}

	public void onChange() {
		updateZones();
	}

	public void updateZones() {
		stage.removeEntity(zone);
		if (menu.getSelection().equals("Attack")) {
			zone = new Zone(grid.getRange(new Node(unit.xcoord, unit.ycoord),
					unit.getTotalWepRange(false)), Zone.ATTACK_DARK);
			stage.addEntity(zone);
		} else if (menu.getSelection().equals("Heal")) {
			zone = new Zone(grid.getRange(new Node(unit.xcoord, unit.ycoord),
					unit.getTotalWepRange(true)), Zone.HEAL_DARK);
			stage.addEntity(zone);
		}
	}

	@Override
	public void onCancel() {
		grid.move(unit.xcoord, unit.ycoord, origX, origY);
		unit.xcoord = origX;
		unit.ycoord = origY;
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
