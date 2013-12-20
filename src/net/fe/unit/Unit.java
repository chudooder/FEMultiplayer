package net.fe.unit;

import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chu.engine.AnimationData;
import chu.engine.Game;
import chu.engine.GriddedEntity;
import chu.engine.Resources;
import chu.engine.anim.Animation;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;
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
	
	public static final float MAP_ANIM_SPEED = 0.2f;

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
		
		sprite.addAnimation("IDLE", new MapAnimation(functionalClassName() + 
				"_map_idle", false));
		sprite.addAnimation("SELECTED", new MapAnimation(functionalClassName() + 
				"_map_selected", false));
		sprite.addAnimation("LEFT", new MapAnimation(functionalClassName() + 
				"_map_side", true));
		sprite.addAnimation("RIGHT", new MapAnimation(functionalClassName() + 
				"_map_side", true));
		sprite.addAnimation("UP", new MapAnimation(functionalClassName() + 
				"_map_up", true));
		sprite.addAnimation("DOWN", new MapAnimation(functionalClassName() + 
				"_map_down", true));

		sprite.setAnimation("IDLE");

		renderDepth = OverworldStage.UNIT_DEPTH;
	}
	
	public String functionalClassName(){
		String prefix = clazz.name;
		if(prefix.equals("Lord")){
			prefix = name;
		}
		return prefix;
	}

	public void move(Path p, Command callback) {
		this.path = p.getCopy();
		path.removeFirst();
		if (path.size() != 0) {
			Node next = path.removeFirst();
			rX = -(next.x - xcoord) * 16;
			rY = -(next.y - ycoord) * 16;
			xcoord = next.x;
			ycoord = next.y;

		}
		this.callback = callback;
		this.renderDepth+=0.001f;
	}
	
	public void beginStep(){
		super.beginStep();
		if(path != null){
			String name;
			if(rX > 0) 		name = "left";
			else if(rX < 0) name = "right";
			else if(rY < 0) name = "down";
			else 			name = "up";
			sprite.setAnimation(name);
		}
	}

	public void onStep() {
		super.onStep();
		float rXOld = rX;
		float rYOld = rY;
		rX = rX - Math.signum(rX) * Game.getDeltaSeconds() * 250;
		rY = rY - Math.signum(rY) * Game.getDeltaSeconds() * 250;
		if (path != null
				&& (rXOld * rX < 0 || rYOld * rY < 0 || (rXOld == rX && rYOld == rY))) {
			if (path.size() == 0) {
				// We made it to destination
				rX = 0;
				rY = 0;
				path = null;
				callback.execute();
				this.renderDepth-=0.001f;
			} else {
				Node next = path.removeFirst();
				rX = -(next.x - xcoord) * 16;
				rY = -(next.y - ycoord) * 16;
				xcoord = next.x;
				ycoord = next.y;
				x = xcoord * 16;
				y = ycoord * 16;
			}
		}
		
	}

	public void endStep() {
		if (dying)
			alpha -= Game.getDeltaSeconds();
		if (alpha < 0) {
			((OverworldStage) stage).setControl(true);
			((OverworldStage) stage).removeUnit(this);
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
		if(Resources.hasTexture(functionalClassName() + "_map_idle")){
			Transform t = new Transform();
			if(sprite.getAnimationName().equals("RIGHT")){
				t.flipHorizontal();
				t.setTranslation(14, 0); //Why do we have to do this?
			}
			//TODO Colors
			Color c = !moved ? getPartyColor() : Color.gray;
			Renderer.drawRectangle(x + 1 + rX, y + 1 + rY, x + 14 + rX,
					y + 14 + rY, renderDepth+0.001f, c);
			
			
			sprite.renderTransformed(x+1+rX, y+1+rY, renderDepth, t);
		} else {
			Color c = !moved ? getPartyColor() : new Color(128, 128, 128);
			c.a = alpha;
			Renderer.drawRectangle(x + 1 + rX, y + 1 + rY, x + 14 + rX,
					y + 14 + rY, OverworldStage.UNIT_DEPTH, c);
			Renderer.drawString("default_med",
					name.charAt(0) + "" + name.charAt(1), x + 2 + rX, y + 1 + rY,
					OverworldStage.UNIT_DEPTH);
			
		}
//		int hpLength = hp * 13 / get("HP");
//		Renderer.drawLine(x + 1, y + 14.5f, x + 1 + hpLength, y + 13.5f, 1,
//				OverworldStage.UNIT_DEPTH - 0.01f, Color.red, Color.green);
	}

	public void setLevel(int lv) {
		if (lv > 20 || lv < 1) {
			return;
		}
		stats.put("Lvl", (float) lv);
		lv--;
		for (String stat : growths.keySet()) {
			float newStat = bases.get(stat)
					+ (float) (lv * growths.get(stat) / 100.0);
			float max = stat.equals("HP") ? 60 : 35;
			stats.put(stat, Math.min(newStat, max));
		}
		fillHp();
	}

	public void fillHp() {
		setHp(get("HP"));
	}

	//Inventory
	public Iterable<Item> getInventory() {
		return inventory;
	}
	
	public int findItem(Item i){
		return inventory.indexOf(i);
	}
	
	public void removeFromInventory(Item item){
		inventory.remove(item);
	}
	
	public void addToInventory(Item item) {
		if(inventory.size() < 4)
			inventory.add(item);
	}
	
	public Set<Integer> getTotalWepRange(boolean staff) {
		Set<Integer> range = new HashSet<Integer>();
		for (Item i : getInventory()) {
			if (!(i instanceof Weapon))
				continue;
			Weapon w = (Weapon) i;
			if (staff == (w.type == Weapon.Type.STAFF) && equippable(w))
				range.addAll(w.range);
		}
		return range;
	}
	
	public void equip(Weapon w) {
		if (equippable(w)) {
			weapon = w;
			if(stage != null){
				((OverworldStage) stage).addCmd("Equip");
				((OverworldStage) stage).addCmd(new UnitIdentifier(this));
				((OverworldStage) stage).addCmd(findItem(w));
			}
			inventory.remove(w);
			inventory.add(0, w);
		}
	}

	public boolean equippable(Weapon w) {
		if(w.pref!= null){
			return name.equals(w.pref);
		}
		return clazz.usableWeapon.contains(w.type);

	}

	public ArrayList<Weapon> equippableWeapons(int range) {
		ArrayList<Weapon> weps = new ArrayList<Weapon>();
		for (Item i : inventory) {
			if (i instanceof Weapon) {
				Weapon w = (Weapon) i;
				if (equippable(w) && w.type != Weapon.Type.STAFF
						&& w.range.contains(range)) {
					weps.add(w);
				}
			}
		}
		return weps;
	}
	
	public ArrayList<Weapon> equippableStaves(int range) {
		ArrayList<Weapon> weps = new ArrayList<Weapon>();
		for (Item i : inventory) {
			if (i instanceof Weapon) {
				Weapon w = (Weapon) i;
				if (equippable(w) && w.type == Weapon.Type.STAFF
						&& w.range.contains(range)) {
					weps.add(w);
				}
			}
		}
		return weps;
	}

	public int equipFirstWeapon(int range) {
		for (int i = 0; i < inventory.size(); i++) {
			Item it = inventory.get(i);
			if (it instanceof Weapon) {
				Weapon w = (Weapon) it;
				if (equippable(w) && w.type != Weapon.Type.STAFF
						&& w.range.contains(range)) {
					equip(w);
					return i;
				}
			}
		}
		return -1;
	}

	public int use(int index) {
		return use(inventory.get(index), true);
	}
	
	public int use(int index, boolean destroy){
		return use(inventory.get(index), destroy);
	}
	
	public int use(Item i){
		return use(i, true);
	}

	public int use(Item i, boolean destroy) {
		int ans = i.use(this);
		if(i.getUses() <= 0 && destroy){
			if(i == weapon){
				weapon = null;
			}
			inventory.remove(i);
		}
		return ans;
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
		if(weapon == null) return 0;
		return weapon.hit + 2 * get("Skl") + get("Lck") / 2
				+ (tempMods.get("Hit") != null ? tempMods.get("Hit") : 0);
	}

	public int avoid() {
		return 2 * get("Spd") + get("Lck") / 2
				+ (tempMods.get("Avo") != null ? tempMods.get("Avo") : 0)
				+ getTerrain().avoidBonus;
	}

	public int crit() {
		if(weapon == null) return 0;
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

	public Terrain getTerrain() {
		return ((OverworldStage) stage).getTerrain(xcoord, ycoord);
	}

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
		sprite.setAnimation("IDLE");
		origX = xcoord;
		origY = ycoord;
	}

	public boolean hasMoved() {
		return moved;
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
		if (dying)
			((OverworldStage) stage).setControl(false);
	}


}
