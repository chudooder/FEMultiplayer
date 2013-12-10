package net.fe.unit;

import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import chu.engine.Game;
import chu.engine.GriddedEntity;
import chu.engine.anim.Renderer;
import net.fe.Command;
import net.fe.Party;
import net.fe.fightStage.*;
import net.fe.overworldStage.*;

public class Unit extends GriddedEntity {
	private HashMap<String, Float> stats;
	private HashMap<String, Integer> bases;
	private int hp;
	private Class clazz;
	private HashMap<String, Integer> growths;
	private Weapon weapon;
	private ArrayList<Item> inventory;
	private HashMap<String, Integer> tempMods;
	public final String name;
	private Party team;
	private boolean dying;
	private float alpha;
	// TODO Rescue

	private boolean moved;
	private Path path;
	private float rX, rY;
	private Command callback;
	
	private int origX, origY;

	public Unit(String name, Class c, HashMap<String, Integer> bases,
			HashMap<String, Integer> growths) {
		super(0, 0);
		this.bases = bases;
		this.growths = growths;
		inventory = new ArrayList<Item>();
		tempMods = new HashMap<String, Integer>();
		this.name = name;
		alpha = 1.0f;
		clazz = c;

		stats = new HashMap<String, Float>();
		for (String s : bases.keySet()) {
			stats.put(s, bases.get(s).floatValue());
		}

		fillHp();
		
		renderDepth = OverworldStage.UNIT_DEPTH;
	}

	public void move(Path p, Command callback) {
		this.path = p.getCopy();
		path.removeFirst();
		if(path.size() != 0) {
			Node next = path.removeFirst();
			rX = -(next.x - xcoord) * 16;
			rY = -(next.y - ycoord) * 16;
			xcoord = next.x;
			ycoord = next.y;
			
		}
		this.callback = callback;
		
	}

	public void onStep() {
		super.onStep();
		rX = rX - Math.signum(rX) * Game.getDeltaSeconds() * 500;
		rY = rY - Math.signum(rY) * Game.getDeltaSeconds() * 500;
		if (path != null && Math.abs(rX + rY) < 1) {
			if (path.size() == 0) {
				// We made it to destination
				rX = 0;
				rY = 0;
				path = null;
				callback.execute();
			} else {
				Node next = path.removeFirst();
				rX = -(next.x - xcoord) * 16;
				rY = -(next.y - ycoord) * 16;
				xcoord = next.x;
				ycoord = next.y;
			}
		}
	}
	
	public void endStep() {
		if(dying) alpha -= Game.getDeltaSeconds();
		if(alpha < 0) {
			((OverworldStage)stage).setControl(true);
			((OverworldStage)stage).removeUnit(this);
		}
	}

	public Unit getCopy() {
		Unit copy = new Unit(name, clazz, bases, growths);
		copy.setLevel(stats.get("Lvl").intValue());
		for (Item i : inventory) {
			copy.addToInventory(i);
		}
		return copy;
	}

	public void render() {
		Color c = !moved? getPartyColor(): Color.gray;
		c.a = alpha;
		Renderer.drawRectangle(x + 1 + rX, y + 1 + rY, x + 14 + rX, y + 14 +rY, OverworldStage.UNIT_DEPTH, c);
		Renderer.drawString("default_med", name.charAt(0) + "", x + 4 + rX, y + 1 + rY, OverworldStage.UNIT_DEPTH);
		int hpLength = hp*13/get("HP");
		Renderer.drawLine(x+1,y+13.5f, x+1+hpLength, y+13.5f, 1, OverworldStage.UNIT_DEPTH-0.01f, Color.red, Color.green);
	}

	public void levelUp() {
		if (stats.get("Lvl") == 20) {
			return;
		}
		stats.put("Lvl", stats.get("Lvl") + 1);
		for (String stat : growths.keySet()) {
			stats.put(stat, stats.get(stat)
					+ (float) (growths.get(stat) / 100.0));
		}
		fillHp();
	}

	public void setLevel(int lv) {
		if (lv > 20 || lv < 1) {
			return;
		}
		stats.put("Lvl", (float) lv);
		lv--;
		for (String stat : growths.keySet()) {
			stats.put(stat, bases.get(stat)
					+ (float) (lv * growths.get(stat) / 100.0));
		}
		fillHp();
	}

	public void fillHp() {
		setHp(get("HP"));
	}

	public void equip(int index) {
		if (equippable(index)){
			weapon = (Weapon) inventory.remove(index);
			inventory.add(0, weapon);
		} else
			throw new IllegalArgumentException("Cannot equip that item");

	}
	
	public void equip(Weapon w){
		if (equippable(w)){
			weapon = w; 
			inventory.remove(w);
			inventory.add(0, w);
		}
	}

	public boolean equippable(int index) {
		if (inventory.get(index) instanceof Weapon) {
			Weapon w = (Weapon) inventory.get(index);
			if (w.pref != null) {
				return name.equals(w.pref);
			}
			return clazz.usableWeapon.contains(w.type);
		}
		return false;
	}
	

	public boolean equippable(Weapon w) {
		return clazz.usableWeapon.contains(w.type);
	}
	
	public void equipFirstWeapon(int range){
		for(Item i : inventory){
			if(i instanceof Weapon){
				Weapon w = (Weapon) i;
				if(w.type != Weapon.Type.STAFF && w.range.contains(range)){
					equip(w);
					return;
				}
			}
		}
	}

	public ArrayList<CombatTrigger> getTriggers() {
		ArrayList<CombatTrigger> triggers = new ArrayList<CombatTrigger>();
		if (clazz.masterSkill != null)
			triggers.add(clazz.masterSkill);
		triggers.addAll(weapon.getTriggers());
		return triggers;
	}

	// Combat statistics
	public int hit() {
		return weapon.hit + 2 * get("Skl") + get("Lck") / 2
				+ (tempMods.get("Hit") != null ? tempMods.get("Hit") : 0);
	}

	public int avoid() {
		return 2 * get("Spd") + get("Lck") / 2
				+ (tempMods.get("Avo") != null ? tempMods.get("Avo") : 0)
				+ getTerrain().avoidBonus;
	}

	public int crit() {
		return weapon.crit + get("Skl") / 2 + clazz.crit
				+ (tempMods.get("Crit") != null ? tempMods.get("Crit") : 0);
	}

	public int dodge() { // Critical avoid
		return get("Lck")
				+ (tempMods.get("Dodge") != null ? tempMods.get("Dodge") : 0);
	}

	// Getter/Setter
	public Class getTheClass() {
		return clazz;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = Math.max(hp, 0);
	}

	public int get(String stat) {
		int ans = stats.get(stat).intValue()
				+ (weapon != null ? weapon.modifiers.get(stat) : 0)
				+ (tempMods.get(stat) != null ? tempMods.get(stat) : 0);
		if (Arrays.asList("Def", "Res").contains(stat)) {
			ans += getTerrain().defenseBonus;
		}
		return ans;
	}

	public int getBase(String stat) {
		return stats.get(stat).intValue();
	}

	public void setTempMod(String stat, int val) {
		tempMods.put(stat, val);
	}

	public void clearTempMods() {
		tempMods.clear();
	}

	public Weapon getWeapon() {
		return weapon;
	}
	
	public Set<Integer> getTotalWepRange(boolean staff){
		Set<Integer> range = new HashSet<Integer>();
		for(Item i: getInventory()){
			if(!(i instanceof Weapon)) continue;
			Weapon w = (Weapon) i;
			if(staff == (w.type == Weapon.Type.STAFF) && equippable(w))
				range.addAll(w.range);
		}
		return range;
	}

	public void addToInventory(Item item) {
		inventory.add(item);
	}

	public Terrain getTerrain() {
		return ((OverworldStage) stage).getTerrain(xcoord, ycoord);
	}

	// Debugging
	public String toString() {
		return name + " HP" + hp + "\n" + stats;
	}

	public Color getPartyColor() {
		return team.getColor();
	}

	public void setParty(Party t) {
		team = t;
	}

	public Party getParty() {
		return team;
	}

	public void moved() {
		moved = true;
		origX = xcoord;
		origY = ycoord;
	}

	public boolean hasMoved() {
		return moved;
	}

	public Iterable<Item> getInventory() {
		return inventory;
	}

	public int getOrigX() {
		return origX;
	}

	public void setOrigX(int origX) {
		this.origX = origX;
	}

	public void setOrigY(int origY) {
		this.origY = origY;
	}

	public int getOrigY() {
		return origY;
	}

	public void setDying(boolean b) {
		dying = b;
		if(dying) ((OverworldStage)stage).setControl(false);
	}

}
