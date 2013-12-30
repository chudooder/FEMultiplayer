package net.fe.overworldStage.context;

import java.util.ArrayList;
import java.util.List;

import chu.engine.Entity;
import chu.engine.anim.Renderer;
import chu.engine.anim.Sprite;
import chu.engine.anim.Transform;
import net.fe.FEResources;
import net.fe.fightStage.FightStage;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.SelectTargetContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.ItemDisplay;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.Weapon;

public class HealTarget extends SelectTargetContext {
	private StaffSelector selector;

	public HealTarget(OverworldStage stage, OverworldContext context, Zone z,
			Unit u) {
		super(stage, context, z, u, true);
		// TODO Positioning
		selector = new StaffSelector(0, 0, new ArrayList<Weapon>());
	}

	@Override
	public void unitSelected(Unit u) {
		unit.equip(selector.getSelected());
		stage.addCmd("Heal");
		stage.addCmd(new UnitIdentifier(u));
		stage.send();

		unit.moved();
		cursor.setXCoord(unit.getXCoord());
		cursor.setYCoord(unit.getYCoord());
		stage.reset();
	}

	public void onLeft() {
		selector.prev();
	}

	public void onRight() {
		selector.next();
	}

	public void updateCursor() {
		super.updateCursor();
		selector.setStaves(unit.equippableStaves(Grid.getDistance(unit,
				getCurrentTarget())));
	}

	public void startContext() {
		super.startContext();
		stage.addEntity(selector);
	}

	public void cleanUp() {
		super.cleanUp();
		stage.removeEntity(selector);
	}

}

class StaffSelector extends Entity {
	private List<Weapon> staves;
	private int index;
	private Sprite leftArrow, rightArrow;

	public StaffSelector(float x, float y, List<Weapon> staves) {
		super(x, y);
		this.staves = staves;
		renderDepth = OverworldStage.MENU_DEPTH;
		rightArrow = new Sprite();
		rightArrow.addAnimation("default",
				FEResources.getTexture("gui_selectArrow"), 8, 8, 6, 6, 0.1f);
		leftArrow = new Sprite();
		leftArrow.addAnimation("default",
				FEResources.getTexture("gui_selectArrow"), 8, 8, 6, 6, 0.1f);
	}
	
	public void onStep(){
		super.onStep();
		leftArrow.update();
		rightArrow.update();
	}

	public void setStaves(List<Weapon> staves) {
		if (staves.size() != this.staves.size()) {
			index = 0;
		}
		this.staves = staves;
	}
	
	public Weapon getSelected(){
		return staves.get(index);
	}

	public void next() {
		index++;
		index %= staves.size();
	}

	public void prev() {
		index--;
		if (index < 0)
			index += staves.size();
	}

	public void render() {
		Renderer.drawRectangle(x, y, x + 90, y + 20, renderDepth,
				FightStage.BORDER_DARK);
		Renderer.drawRectangle(x+1, y+1, x + 89, y + 19, renderDepth,
				FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x+2, y+2, x + 88, y + 18, renderDepth,
				FightStage.NEUTRAL);
		new ItemDisplay((int) x + 12, (int) y + 1, staves.get(index), false).render();
		leftArrow.render(x+3, y+6, renderDepth);
		Transform t = new Transform();
		t.flipHorizontal();
		rightArrow.render(x + 87, y+6, renderDepth, t);
	}

}
