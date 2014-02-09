package net.fe.unit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import net.fe.Command;
import net.fe.FEResources;
import net.fe.Party;
import net.fe.fightStage.CombatTrigger;
import net.fe.overworldStage.DoNotDestroy;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.OverworldStage;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.Terrain;

import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.GriddedEntity;
import chu.engine.anim.Renderer;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;

public class Unit extends GriddedEntity implements Serializable, DoNotDestroy{
	
	private static final long serialVersionUID = -5101031417704315547L;
	private HashMap<String, Float> stats;
	private HashMap<String, Integer> bases;
	private ArrayList<CombatTrigger> skills;
	private int hp;
	private Class clazz;
	private char gender;
	private HashMap<String, Integer> growths;
	private Weapon weapon;
	private ArrayList<Item> inventory;
	public final String name;
	private Party team;
	private transient HashMap<String, Integer> tempMods;
	private transient HashMap<String, Integer> battleStats;
	private transient Set<Unit> assist;
	private transient boolean dying;
	private transient float alpha;
	private transient Unit rescuedUnit;
	private transient boolean moved;
	private transient Path path;
	private transient float rX, rY;
	private transient Command callback;
	private boolean rescued;
	private float counter;

	private int origX, origY;
	
	public static final float MAP_ANIM_SPEED = 0.2f;
	public static final int MOVE_SPEED = 250;

	public Unit(String name, Class c, char gender, HashMap<String, Integer> bases,
			HashMap<String, Integer> growths) {
		super(0, 0);
		this.bases = bases;
		this.growths = growths;
		this.gender = gender;
		inventory = new ArrayList<Item>();
		tempMods = new HashMap<String, Integer>();
		assist = new HashSet<Unit>();
		skills = new ArrayList<CombatTrigger>();
		battleStats = new HashMap<String, Integer>();
        battleStats.put("Kills", 0);
        battleStats.put("Assists", 0);
        battleStats.put("Damage", 0);
        battleStats.put("Healing", 0);
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

		renderDepth = ClientOverworldStage.UNIT_DEPTH;
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        tempMods = new HashMap<String, Integer>();
        assist = new HashSet<Unit>();
        skills = new ArrayList<CombatTrigger>();
        battleStats = new HashMap<String, Integer>();
        battleStats.put("Kills", 0);
        battleStats.put("Assists", 0);
        battleStats.put("Damage", 0);
        battleStats.put("Healing", 0);
        if(Game.glContextExists()) {
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
        }
        alpha = 1.0f;
    }
	
	public String functionalClassName(){
		String prefix = clazz.name;
		if(prefix.equals("Lord")){
			prefix = name;
		}
		if(gender != '-'){
			prefix += gender;
		}
		return prefix.toLowerCase();
	}

	public void move(Path p, Command callback) {
		this.path = p.getCopy();
		this.callback = callback;
	}
	
	public void rescue(Unit u){
		final int oldX = u.xcoord;
		final int oldY = u.ycoord;
		rescuedUnit = u;
		rescuedUnit.rescued = true;
		final OverworldStage grid = (OverworldStage) stage;
		Path p = new Path();
		p.add(new Node(this.xcoord, this.ycoord));
		rescuedUnit.move(p, new Command(){
			@Override
			public void execute() {
				rescuedUnit.xcoord = oldX;
				rescuedUnit.ycoord = oldY;
				grid.removeUnit(rescuedUnit);
			}
		});
	}

	
	public void drop(int x, int y){
		if(rescuedUnit == null) return;
		rescuedUnit.rescued = false;
		final OverworldStage grid = (OverworldStage) stage;
		grid.addUnit(rescuedUnit, x, y);
		rescuedUnit.rX = this.x - x * 16;
		rescuedUnit.rY = this.y - y * 16;
		rescuedUnit = null;
	}
	
	
	public void give(Unit u){
		if(rescuedUnit == null) return;
		if(u.rescuedUnit() != null) return;
		u.setRescuedUnit(rescuedUnit);
		rescuedUnit = null;
	}
	
	public void beginStep(){
		super.beginStep();
		if(path != null){
			String name;
			if(rX > 0) 		name = "left";
			else if(rX < 0) name = "right";
			else if(rY > 0) name = "up";
			else 			name = "down";
			sprite.setAnimation(name);
		}
		renderDepth = calcRenderDepth();
	}
	
	private float calcRenderDepth(){
		float depth = ClientOverworldStage.UNIT_DEPTH;
		if(rescued){
			return depth-0.0001f;
		}
		float highlightDiff = (ClientOverworldStage.UNIT_DEPTH - ClientOverworldStage.UNIT_MAX_DEPTH)/2;
		Grid g = ((ClientOverworldStage) stage).grid;
		float yDiff = highlightDiff/g.width;
		float xDiff = yDiff/g.height;
		
		if(path!=null) depth -= highlightDiff;
		if(((ClientOverworldStage) stage).getHoveredUnit() == this) depth -= highlightDiff;
		depth -= ycoord*yDiff;
		depth -= (g.width-xcoord)*xDiff;
		return depth;
	}

	public void onStep() {
		super.onStep();
		float rXOld = rX;
		float rYOld = rY;
		rX = rX - Math.signum(rX) * Game.getDeltaSeconds() * 250;
		rY = rY - Math.signum(rY) * Game.getDeltaSeconds() * 250;
		if(rXOld * rX < 0 || rYOld * rY < 0 || (rXOld == rX && rYOld == rY)){
			rX = 0;
			rY = 0;
			if (path != null) {
				if (path.size() == 0) {
					// We made it to destination					
					path = null;
					callback.execute();
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
	}

	public void endStep() {
		if (dying)
			alpha -= Game.getDeltaSeconds();
		if (alpha < 0) {
			((ClientOverworldStage) stage).setControl(true);
			((ClientOverworldStage) stage).removeUnit(this);
			dying = false;
		}
	}

	Unit getCopy() {
		Unit copy = new Unit(name, clazz, gender, bases, growths);
		copy.setLevel(stats.get("Lvl").intValue());
		for (Item i : inventory) {
			copy.addToInventory(i);
		}
		return copy;
	}

	public void render() {
		ClientOverworldStage cs = (ClientOverworldStage)stage;
		Renderer.translate(-cs.camX, -cs.camY);
		Renderer.addClip(0, 0, 368, 240, true);
		
		if(FEResources.hasTexture(functionalClassName().toLowerCase() + "_map_idle")){
			Transform t = new Transform();
			if(sprite.getAnimationName().equals("RIGHT")){
				t.flipHorizontal();
				t.setTranslation(14, 0); //Why do we have to do this?
			}
			//TODO Colors
			Color mod = new Color(1.0f, 1.0f, 1.0f, 1.0f);
			if(dying)
				mod.a = alpha;
			t.setColor(mod);
			if(moved) {
				sprite.render(x+1+rX, y+1+rY, renderDepth, t, new ShaderArgs("greyscale"));
			} else {
				if(team.getColor().equals(Party.TEAM_RED)) {
					sprite.render(x+1+rX, y+1+rY, renderDepth, t, new ShaderArgs("paletteSwap"));
				} else {
					sprite.render(x+1+rX, y+1+rY, renderDepth, t);
				}
			}
		} else {
			Color c = !moved ? new Color(getPartyColor()) : new Color(128, 128, 128);
			c.a = alpha;
			Renderer.drawRectangle(x + 1 + rX, y + 1 + rY, x + 14 + rX,
					y + 14 + rY, ClientOverworldStage.UNIT_DEPTH, c);
			Renderer.drawString("default_med",
					name.charAt(0) + "" + name.charAt(1), x + 2 + rX, y + 1 + rY,
					ClientOverworldStage.UNIT_DEPTH);
			
		}
		if(rescuedUnit!=null){
			counter+=Game.getDeltaSeconds();
			counter%=1;
			if(counter > 0.5){
				Renderer.render(FEResources.getTexture("rescue"), 
						0, 0, 1, 1, x+9, y+7, x+9+8, y+7+8, renderDepth);
			}
		}
		
		Renderer.removeClip();
		Renderer.translate(cs.camX, cs.camY);
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
	
	public void addSkill(CombatTrigger t) {
		skills.add(t);
	}

	public void fillHp() {
		setHp(get("HP"));
	}

	//Inventory
	public List<Item> getInventory() {
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
				((ClientOverworldStage) stage).addCmd("EQUIP");
				((ClientOverworldStage) stage).addCmd(new UnitIdentifier(this));
				((ClientOverworldStage) stage).addCmd(findItem(w));
			}
			inventory.remove(w);
			inventory.add(0, w);
		}
	}
	
	// For use in command message processing only
	public void equip(int i) {
		Weapon w = (Weapon)inventory.get(i);
		if (equippable(w)) {
			weapon = w;
			inventory.remove(w);
			inventory.add(0, w);
		}
	}
	
	public void unequip(){
		weapon = null;
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
	
	public void initializeEquipment(){
		for(Item it: inventory){
			if(it instanceof Weapon){
				if(equippable((Weapon)it)){
					equip((Weapon) it);
					break;
				}
			}
		}
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
	
	public void reEquip(){
		for (int i = 0; i < inventory.size(); i++) {
			Item it = inventory.get(i);
			if (it instanceof Weapon) {
				Weapon w = (Weapon) it;
				if (equippable(w)) {
					equip(w);
					return;
				}
			}
		}
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
			inventory.remove(i);
			if(i == weapon){
				weapon = null;
				reEquip();
			}
			
		}
		return ans;
	}

	public ArrayList<CombatTrigger> getTriggers() {
		ArrayList<CombatTrigger> triggers = new ArrayList<CombatTrigger>();
		triggers.addAll(skills);
		if (clazz.masterSkill != null)
			triggers.add(clazz.masterSkill);
		if(weapon!=null)
			triggers.addAll(weapon.getTriggers());
		return triggers;
	}
	
	//Development
	public static int getExpCost(int level){
		return level * 50 + 500;
	}
	
	public int squeezeExp(){
		int exp = 0;
		while(get("Lvl") != 1){
			exp += getExpCost(get("Lvl"));
			setLevel(get("Lvl") - 1);
		}
		return exp;
	}
	
	public int squeezeGold(){
		int gold = 0;
		ListIterator<Item> items = inventory.listIterator();
		while(items.hasNext()){
			Item i = items.next();
			boolean remove = true;
			if(i instanceof Weapon){
				Weapon w = (Weapon) i;
				if(w.pref != null){
					remove = false;
				}
			}
			if(remove){
				items.remove();
				gold+= i.getCost();
			}
		}
		return gold;
	}
	
	public void addBattleStat(String stat, int add) {
		battleStats.put(stat, battleStats.get(stat) + add);
	}
	
	public int getBattleStat(String stat) {
		return battleStats.get(stat);
	}
	
	public void reportBattleStats() {
		for(String s : battleStats.keySet()) {
			System.out.print(s+": ");
			System.out.print(battleStats.get(s)+" ");
		}
		System.out.println();
	}
	
	public Set<Unit> getAssisters() {
		return assist;
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
				+ getTerrain().getAvoidBonus(this);
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
			ans += getTerrain().getDefenseBonus(this);
		}
		if((stat.equals("Spd") || stat.equals("Skl")) && rescuedUnit!=null){
			ans/=2;
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
		if(stage == null) return Terrain.PLAIN;
		return ((OverworldStage) stage).getTerrain(xcoord, ycoord);
	}
	
	public String toString() {
		return name + " HP" + hp + " #" + hashCode() + " \n" + stats;
	}

	public Color getPartyColor() {
		if(team == null) return Party.TEAM_BLUE;
		return team.getColor();
	}

	public void setParty(Party t) {
		team = t;
	}

	public Party getParty() {
		return team;
	}

	public void setMoved(boolean status) {
		moved = status;
		if(moved) {
			sprite.setAnimation("IDLE");
			origX = xcoord;
			origY = ycoord;
		}
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
			((ClientOverworldStage) stage).setControl(false);
	}
	
	public boolean isDying(){
		return dying;
	}
	
	public Unit rescuedUnit(){
		return rescuedUnit;
	}

	public void setRescuedUnit(Unit unit) {
		rescuedUnit = unit;
	}
}
